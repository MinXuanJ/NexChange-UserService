package com.nus.nexchange.userservice.infrastructure.messaging;

import com.nus.nexchange.userservice.api.dto.UserDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final KafkaTemplate<String,Object> kafkaObjectTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, KafkaTemplate<String,Object> kafkaObjectTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaObjectTemplate = kafkaObjectTemplate;
    }

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    public void sendDTO(String topic,Object DTO) {
        kafkaObjectTemplate.send(topic,DTO);
    }
}
