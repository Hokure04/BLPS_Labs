package org.example.blps_lab1.adapters.saga;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.adapters.saga.events.failures.CertificateGenerationFailedEvent;
import org.example.blps_lab1.adapters.saga.events.failures.CertificateSendFailedEvent;
import org.example.blps_lab1.adapters.saga.events.failures.FileUploadFailedEvent;
import org.example.blps_lab1.adapters.saga.events.success.CertificateGeneratedEvent;
import org.example.blps_lab1.adapters.saga.events.success.CertificateSentEvent;
import org.example.blps_lab1.adapters.saga.events.success.CourseCompletedEvent;
import org.example.blps_lab1.adapters.saga.events.success.FileUploadedEvent;
import org.example.blps_lab1.configuration.KafkaUser;
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
            File pdf = certificateGenerator.generateCertificate("CourseName", ev.getUserId().toString(), null);
            publisher.publishEvent(new CertificateGeneratedEvent(ev.getUserId(), ev.getCourseId(), pdf));
        } catch (Exception ex) {
            publisher.publishEvent(new CertificateGenerationFailedEvent(ev.getUserId(), ev.getCourseId(), ex));
        }
    }

    @EventListener
    public void handle(CertificateGeneratedEvent ev) {
        try {
            simpleStorageService.uploadFile(ev.getUserId().toString(), ev.getCourseId().toString(), ev.getPdf());
            publisher.publishEvent(new FileUploadedEvent(ev.getUserId(), ev.getCourseId(), ev.getPdf()));
        } catch (Exception ex) {
            publisher.publishEvent(new FileUploadFailedEvent(ev.getUserId(), ev.getCourseId(), ex));
        }
    }

    @EventListener
    public void handle(FileUploadedEvent ev) {
        try {
            messageProducer.sendMessage("certificate-topic", new KafkaUser(ev.getUserId().toString(), null, null));
            publisher.publishEvent(new CertificateSentEvent(ev.getUserId(), ev.getCourseId()));
        } catch (Exception ex) {
            publisher.publishEvent(new CertificateSendFailedEvent(ev.getUserId(), ev.getCourseId(), ex));
        }
    }

    @EventListener
    public void handle(CertificateSentEvent ev) {
        emailService.sendCertificateToUser(ev.getUserId().toString(), /* файл */ null);
    }

    @EventListener
    public void handle(CertificateGenerationFailedEvent ev) {
        emailService.informMinioFailure(ev.getUserId().toString());
        log.error("Saga step failed: generation", ev.getException());
    }

    @EventListener
    public void handle(FileUploadFailedEvent ev) {
        emailService.informMinioFailure(ev.getUserId().toString());
        log.error("Saga step failed: upload", ev.getException());
    }

    @EventListener
    public void handle(CertificateSendFailedEvent ev) {
        emailService.informMinioFailure(ev.getUserId().toString());
        log.error("Saga step failed: send", ev.getException());
    }
}
