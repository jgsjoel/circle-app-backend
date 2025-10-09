package com.chat.user.services;

import com.chat.user.config.RabbitConfig;
import com.chat.user.dto.LastSeenDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RabbitConsumer {

    private UserService userService;

    @RabbitListener(queues = {RabbitConfig.LAST_SEEN_QUEUE})
    public void consumeMessage(LastSeenDto lastSeenDto){
        userService.updateLastSeen(lastSeenDto);
    }

}
