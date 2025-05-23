package org.example.blps_lab1.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    private MessageProducer messageProducer;

    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message) {
        var user = new User();
        user.setUsername(message);
        user.setPassword(message);
        messageProducer.sendMessage("reg-users", user);
        return "Message sent: " + message;
    }

}