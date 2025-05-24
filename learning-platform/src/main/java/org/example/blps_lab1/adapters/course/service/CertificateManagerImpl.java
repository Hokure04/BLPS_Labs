package org.example.blps_lab1.adapters.course.service;

import java.util.UUID;

import org.example.blps_lab1.adapters.saga.events.success.CourseCompletedEvent;
import org.example.blps_lab1.core.domain.auth.UserXml;
import org.example.blps_lab1.core.exception.course.InvalidFieldException;
import org.example.blps_lab1.core.ports.course.CertificateGenerator;
import org.example.blps_lab1.core.ports.course.CertificateManager;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.example.blps_lab1.core.ports.email.EmailService;
import org.example.blps_lab1.core.ports.sss.SimpleStorageService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Slf4j
public class CertificateManagerImpl implements CertificateManager {
    private final CertificateGenerator certificateGenerator;
    private final NewCourseService courseService;
    private final SimpleStorageService simpleStorageService;
    private final EmailService emailService;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationEventPublisher publisher;

    public CertificateManagerImpl(CertificateGenerator certificateGenerator, NewCourseService courseService, SimpleStorageService simpleStorageService, EmailService emailService, PlatformTransactionManager transactionManager, ApplicationEventPublisher publisher) {
        this.certificateGenerator = certificateGenerator;
        this.courseService = courseService;
        this.simpleStorageService = simpleStorageService;
        this.emailService = emailService;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.publisher = publisher;
    }

    @Override
    public void getCertificate(UserXml user, UUID courseUUID) {
        var course = courseService.getCourseByUUID(courseUUID);
        boolean allModulesCompleted = courseService.isCourseFinished(courseUUID);
        if (!allModulesCompleted) {
            throw new InvalidFieldException("Курс не пройден до конца");
        }
        publisher.publishEvent(new CourseCompletedEvent(user, course));
    }

}
