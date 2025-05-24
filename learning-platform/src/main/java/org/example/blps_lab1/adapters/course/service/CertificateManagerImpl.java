package org.example.blps_lab1.adapters.course.service;

import java.util.UUID;

import org.example.blps_lab1.adapters.saga.events.success.CourseCompletedEvent;
import org.example.blps_lab1.core.domain.auth.UserXml;
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
        //TODO проверка прошел ли пользователь все курсы -> если нет, throw шо нить,

            publisher.publishEvent(new CourseCompletedEvent(user, UUID.randomUUID()));
    }
//        transactionTemplate.execute(status -> {
//
//            var course = courseService.getCourseByUUID(courseUUID);
//
//            boolean allModulesCompleted = courseService.isCourseFinished(courseUUID);
//
//            if (!allModulesCompleted) {
//                throw new InvalidFieldException("Курс не пройден до конца");
//            }
//
//            try {
//                var certificatePdf = certificateGenerator.generateCertificate(course.getName(), user.getUsername(), null);
//                saveToSimpleStorageService(user, course, certificatePdf);
//                emailService.sendCertificateToUser(user.getUsername(), certificatePdf);
//            } catch (Exception e) {
//                log.error("Error while creating the certificate", e);
//                sendAboutException(user.getUsername());
//            }
//
//            return 0;
//        });
//    }
//
//    private void saveToSimpleStorageService(UserXml user, NewCourse course, File file) {
//        StringBuilder filename = new StringBuilder();
//        filename.append(user.getUsername()).append(course.getName());
//        try {
//            simpleStorageService.uploadFile(user.getUsername(), filename.toString(), file);
//        } catch (Exception e) {
//            log.error("Error while uploading file to minio");
//        }
//    }
//
//    private void sendAboutException(String email) {
//        emailService.informMinioFailure(email);
//        throw new RuntimeException("Ошибка сервиса при создании отчета");//ошибку эту оставить, необходимо, чтобы оно сработала даже в случае успешного письма пользователю об ошибке
//    }
}
