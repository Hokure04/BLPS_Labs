package org.example.blps_lab1.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    public void sendMessage(String topic, User message) {
        kafkaTemplate.send(topic, message);
    }

}