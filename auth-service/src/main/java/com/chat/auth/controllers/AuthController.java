package com.chat.auth.controllers;

import com.chat.auth.dtos.RequestDto;
import com.chat.auth.dtos.UserDto;
import com.chat.auth.groups.Request;
import com.chat.auth.groups.Validate;
import com.chat.auth.services.OtpService;
import com.chat.auth.services.UserService;
import com.chat.auth.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final OtpService otpService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(JwtUtil jwtUtil, OtpService otpService,UserService userService) {
        this.otpService = otpService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    //works
    @PostMapping("/request-otp")
    public ResponseEntity<?> getOtp(@Validated(Request.class) @RequestBody RequestDto request) {
        String otp = String.valueOf(otpService.generateOtp());
        otpService.saveOtp(request.getMobile(), otp);
        System.out.println("OTP IS :" +otp);

        // Call SMS service here
        String x = otpService.sendMessage(request.getMobile(), otp).block();
        System.out.println(x);

        return ResponseEntity.ok("OTP sent");
    }

    //works
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Validated(Validate.class) @RequestBody RequestDto request) {
        boolean isValid = otpService.validateOtp(request.getMobile(), request.getOtp());

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        }

        // Register user via User Service
        //pass username, mobile
        Mono<UserDto> user = userService.createUser(request);

        otpService.deleteOtp(request.getMobile());

        // Generate JWT
        String token = jwtUtil.generateToken(user.block().getId());
        return new ResponseEntity<Map<String,String>>(Map.of("token",token),HttpStatus.OK);
    }



}

