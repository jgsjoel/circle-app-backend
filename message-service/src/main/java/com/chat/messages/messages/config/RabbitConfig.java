package com.chat.messages.messages.config;

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

    public static final String MESSAGE_EXCHANGE = "message.exchange";

    public final static String MESSAGE_PROCESS_QUEUE = "message.process"; //published by this message socket service

    public final static String MESSAGE_RESPONSE_QUEUE = "message.response";//listened by this message service

    public final static String UNDELIVERED_REQUEST_QUEUE = "Undelivered.request";//published by this message service

    public final static String UNDELIVERED_RESPONSE_QUEUE = "Undelivered.response";//listened by this message service

    public final static String MESSAGE_STATUS_RESPONSE_QUEUE = "msgstat.response";//listened by this web socket service

    public final static String MESSAGE_STATUS_PROCESS_QUEUE = "msgstat.request";//listened by this web socket service


    @Bean
    public DirectExchange chatExchange() {
        return new DirectExchange(MESSAGE_EXCHANGE);
    }

    @Bean
    public Queue msgStatProcessQueue() {
        return new Queue(MESSAGE_STATUS_PROCESS_QUEUE, false);
    }

    @Bean
    public Queue messageProcessQueue() {
        return new Queue(RabbitConfig.MESSAGE_PROCESS_QUEUE, false);
    }

    @Bean
    public Queue undeliveredResponseQueue() {
        return new Queue(RabbitConfig.UNDELIVERED_REQUEST_QUEUE, false);
    }

    @Bean
    public Binding bindingProcessQueue(Queue messageProcessQueue, DirectExchange chatExchange) {
        return BindingBuilder.bind(messageProcessQueue)
                .to(chatExchange)
                .with("message.process");
    }

    @Bean
    public Binding bindingUndeliveredResponseQueue(Queue undeliveredResponseQueue, DirectExchange chatExchange) {
        return BindingBuilder.bind(undeliveredResponseQueue)
                .to(chatExchange)
                .with("undelivered.response");
    }

    @Bean
    public Binding bindingMsgStatRequestQueue(Queue msgStatProcessQueue, DirectExchange messageExchange) {
        return BindingBuilder.bind(msgStatProcessQueue)
                .to(messageExchange)
                .with(MESSAGE_STATUS_PROCESS_QUEUE);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}
