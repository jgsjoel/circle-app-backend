package com.chat.websocket.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMqConfig {

    public static final String MESSAGE_EXCHANGE = "message.exchange";

    public final static String MESSAGE_PROCESS_QUEUE = "message.process"; //published by this web socket service

    public final static String MESSAGE_RESPONSE_QUEUE = "message.response";//listened by this web socket service

    public final static String UNDELIVERED_REQUEST_QUEUE = "undelivered.request";//published by this web socket service

    public final static String UNDELIVERED_RESPONSE_QUEUE = "undelivered.response";//listened by this web socket service

    @Bean
    public DirectExchange messageExchange() {
        return new DirectExchange(MESSAGE_EXCHANGE, true, false);
        // durable = true, autoDelete = false
    }

    @Bean
    public Queue messageResponseQueue() {
        return new Queue(MESSAGE_RESPONSE_QUEUE, false);
    }


    @Bean
    public Queue undeliveredResponseQueue() {
        return new Queue(UNDELIVERED_RESPONSE_QUEUE, false);
    }

    @Bean
    public Binding bindingResponseQueue(Queue messageResponseQueue, DirectExchange messageExchange) {
        return BindingBuilder.bind(messageResponseQueue)
                .to(messageExchange)
                .with("message.response"); // routing key
    }


    @Bean
    public Binding bindingUndeliveredResponseQueue(Queue undeliveredResponseQueue, DirectExchange messageExchange) {
        return BindingBuilder.bind(undeliveredResponseQueue)
                .to(messageExchange)
                .with("undelivered.response");
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
