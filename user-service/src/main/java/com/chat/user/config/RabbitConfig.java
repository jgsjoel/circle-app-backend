package com.chat.user.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String LAST_SEEN_EXCHANGE = "lstSeen.exchange";

    public final static String LAST_SEEN_QUEUE = "lstSeen.process"; //published by this message socket service

    @Bean
    public DirectExchange lstSeenExchange() {
        return new DirectExchange(LAST_SEEN_EXCHANGE);
    }

    @Bean
    public Queue lastSeenQueue() {
        return new Queue(RabbitConfig.LAST_SEEN_QUEUE, false);
    }


    @Bean
    public Binding bindingProcessQueue(Queue lastSeenQueue, DirectExchange lstSeenExchange) {
        return BindingBuilder.bind(lastSeenQueue)
                .to(lstSeenExchange)
                .with(LAST_SEEN_QUEUE);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}
