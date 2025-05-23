package labs.blps.kafkalistenerservice.config;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @KafkaListener(topics = "reg-users", groupId = "my-group-id")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }

}