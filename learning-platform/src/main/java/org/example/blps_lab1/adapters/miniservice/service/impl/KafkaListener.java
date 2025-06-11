package org.example.blps_lab1.adapters.miniservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.adapters.miniservice.model.MiniUser;
import org.example.blps_lab1.adapters.miniservice.service.RegisterService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class KafkaListener {
    private final RegisterService registerService;

    @EventListener
    public void handle(MiniUser miniUser) {
        log.info("Received MiniUser: {}", miniUser.toString());
        registerService.register(miniUser);
    }
}
