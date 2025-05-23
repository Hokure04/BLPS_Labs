package labs.blps.kafkalistenerservice.config;

import labs.blps.kafkalistenerservice.model.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @KafkaListener(topics = "reg-users", groupId = "group-id")
    public void listen(User user) {
        System.out.println("Received message: " + user.toString());
    }

}