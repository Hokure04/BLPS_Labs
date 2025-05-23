package org.example.blps_lab1.adapters.course.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.adapters.saga.CourseCompletedEvent;
import org.example.blps_lab1.configuration.MessageProducer;
import org.example.blps_lab1.core.ports.course.CertificateGenerator;
import org.example.blps_lab1.core.ports.email.EmailService;
import org.example.blps_lab1.core.ports.sss.SimpleStorageService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
@AllArgsConstructor
public class SagaListeners {
    private final CertificateGenerator certificateGenerator;
    private final SimpleStorageService simpleStorageService;
    private final MessageProducer messageProducer;
    private final EmailService emailService;
    private final ApplicationEventPublisher publisher;


    @EventListener
    public void handle(CourseCompletedEvent ev) {
        try {
            System.err.println("публишер");
            File pdf = certificateGenerator.generateCertificate("CourseName", ev.getUserId().toString(), null);
//            publisher.publishEvent(new CertificateGeneratedEvent(ev.getUserId(), ev.getCourseId(), pdf));
        } catch (Exception ex) {
//            publisher.publishEvent(new CertificateGenerationFailedEvent(ev.getUserId(), ev.getCourseId(), ex));
        }
    }
}
