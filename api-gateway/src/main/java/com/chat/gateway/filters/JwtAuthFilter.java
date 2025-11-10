package com.chat.gateway.filters;

import com.chat.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        String upgradeHeader = exchange.getRequest().getHeaders().getFirst("Upgrade");

        // Skip for auth/public routes
        if (path.startsWith("/auth") || path.startsWith("/public")) {
            System.out.println("Skipping JWT auth for path: " + path);
            return chain.filter(exchange);
        }

        String token = null;

        // If it's a WebSocket handshake
        if ("websocket".equalsIgnoreCase(upgradeHeader)) {
            token = exchange.getRequest().getQueryParams().getFirst("token");
        } else {
            // Standard HTTP request
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        if (token == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing JWT token"));
        }

        if (!jwtUtil.validateToken(token)) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token"));
        }

        Claims claims = jwtUtil.getClaims(token);

        // Inject user id into header
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("X-User-Id", claims.getSubject())
                        .build())
                .build();

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
