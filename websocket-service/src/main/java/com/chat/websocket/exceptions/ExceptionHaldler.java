package com.chat.websocket.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@Slf4j
public class ExceptionHaldler {
    @MessageExceptionHandler(MessageConversionException.class)
    public void handleConversionException(MessageConversionException ex, Message<?> message) {
        log.error("Invalid message received (conversion): {}", ex.getMessage(), ex);
//        log.debug("Message payload: {}", message.getPayload());
    }

    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidationException(MethodArgumentNotValidException ex, Message<?> message) {
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> log.error("Invalid field '{}' : {}", error.getField(), error.getDefaultMessage()));
//        log.debug("Message payload: {}", message.getPayload());
        // Request is killed here; controller method is never called
    }
}
