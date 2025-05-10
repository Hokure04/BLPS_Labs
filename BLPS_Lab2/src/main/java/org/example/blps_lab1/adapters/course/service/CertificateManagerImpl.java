package org.example.blps_lab1.adapters.course.service;

import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.core.domain.auth.UserXml;
import org.example.blps_lab1.core.domain.course.nw.NewCourse;
import org.example.blps_lab1.core.exception.course.InvalidFieldException;
import org.example.blps_lab1.core.ports.course.CertificateGenerator;
import org.example.blps_lab1.core.ports.course.CertificateManager;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.example.blps_lab1.core.ports.email.EmailService;
import org.example.blps_lab1.core.ports.sss.SimpleStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.File;
import java.util.UUID;

@Service
@Slf4j
public class CertificateManagerImpl implements CertificateManager {
    private final CertificateGenerator certificateGenerator;
    private final NewCourseService courseService;
    private final SimpleStorageService simpleStorageService;
    private final EmailService emailService;
    private final PlatformTransactionManager transactionManager;

    public CertificateManagerImpl(CertificateGenerator certificateGenerator, NewCourseService courseService, SimpleStorageService simpleStorageService, EmailService emailService, PlatformTransactionManager transactionManager) {
        this.certificateGenerator = certificateGenerator;
        this.courseService = courseService;
        this.simpleStorageService = simpleStorageService;
        this.emailService = emailService;
        this.transactionManager = transactionManager;
    }

    @Override
    public void getCertificate(UserXml user, UUID courseUUID) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("getCertificate");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            var course = courseService.getCourseByUUID(courseUUID);

            boolean allModulesCompleted = courseService.isCourseFinished(courseUUID);

            if (!allModulesCompleted) {
                throw new InvalidFieldException("Курс не пройден до конца");
            }

            try {
                var certificatePdf = certificateGenerator.generateCertificate(course.getName(), user.getUsername(), null);
                saveToSimpleStorageService(user, course, certificatePdf);
                emailService.sendCertificateToUser(user.getUsername(), certificatePdf);
            } catch (Exception e) {
                log.error("Error while creating the certificate", e);
                sendAboutException(user.getUsername());
            }
            transactionManager.commit(status);
        }catch (Exception e){
            log.error("transaction failed, rolling back");
            transactionManager.rollback(status);
            throw e;
        }
    }

    private void saveToSimpleStorageService(UserXml user, NewCourse course, File file) {
        StringBuilder filename = new StringBuilder();
        filename.append(user.getUsername()).append(course.getName());
        try {
            simpleStorageService.uploadFile(user.getUsername(), filename.toString(), file);
        } catch (Exception e) {
            log.error("Error while uploading file to minio");
        }
    }

    private void sendAboutException(String email) {
        emailService.informMinioFailure(email);
        throw new RuntimeException("Ошибка сервиса при создании отчета");//ошибку эту оставить, необходимо, чтобы оно сработала даже в случае успешного письма пользователю об ошибке
    }
}
