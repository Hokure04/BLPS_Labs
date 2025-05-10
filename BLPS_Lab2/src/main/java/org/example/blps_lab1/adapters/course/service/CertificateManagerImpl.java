package org.example.blps_lab1.adapters.course.service;

import java.io.File;
import java.util.UUID;

import org.example.blps_lab1.core.domain.auth.UserXml;
import org.example.blps_lab1.core.domain.course.CourseProgressId;
import org.example.blps_lab1.core.domain.course.nw.NewCourse;
import org.example.blps_lab1.core.exception.course.InvalidFieldException;
import org.example.blps_lab1.core.ports.course.CertificateGenerator;
import org.example.blps_lab1.core.ports.course.CertificateManager;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.example.blps_lab1.core.ports.email.EmailService;
import org.example.blps_lab1.core.ports.sss.SimpleStorageService;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class CertificateManagerImpl implements CertificateManager {
    private CertificateGenerator certificateGenerator;
    private NewCourseService courseService;
    private SimpleStorageService simpleStorageService;
    private EmailService emailService;

    @Override
    public void getCertificate(UserXml user, UUID courseUUID) {

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
