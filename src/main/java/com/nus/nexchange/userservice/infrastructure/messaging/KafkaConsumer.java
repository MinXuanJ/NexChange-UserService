package com.nus.nexchange.userservice.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "topic-name"
//    ,groupId="group-name"
    )
    public void listen(String message) {
        System.out.println(message);
    }
}
