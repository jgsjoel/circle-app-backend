package com.chat.fcm_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String FCM_MESSAGE_EXCHANGE = "fcm.message.exchange";

    public final static String MESSAGE_PROCESS_QUEUE = "fcm.process"; //published by this web socket service

    @Bean
    public DirectExchange fcmExchange() {
        return new DirectExchange(FCM_MESSAGE_EXCHANGE, true, false);
        // durable = true, autoDelete = false
    }

    @Bean
    public Queue undeliveredResponseQueue() {
        return new Queue(MESSAGE_PROCESS_QUEUE, false);
    }

    @Bean
    public Binding bindingResponseQueue(Queue undeliveredResponseQueue, DirectExchange fcmExchange) {
        return BindingBuilder.bind(undeliveredResponseQueue)
                .to(fcmExchange)
                .with("fcm.process"); // routing key
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate template(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }



}
