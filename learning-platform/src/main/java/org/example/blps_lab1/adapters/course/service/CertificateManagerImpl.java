package org.example.blps_lab1.adapters.course.service;

import java.io.File;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.example.blps_lab1.adapters.saga.CourseCompletedEvent;
import org.example.blps_lab1.configuration.MessageProducer;
import org.example.blps_lab1.configuration.User;
import org.example.blps_lab1.core.domain.auth.UserXml;
import org.example.blps_lab1.core.domain.course.Course;
import org.example.blps_lab1.core.domain.course.CourseProgress;
import org.example.blps_lab1.core.domain.course.CourseProgressId;
import org.example.blps_lab1.adapters.db.course.CourseProgressRepository;
import org.example.blps_lab1.core.ports.course.CertificateGenerator;
import org.example.blps_lab1.core.ports.course.CertificateManager;
import org.example.blps_lab1.core.ports.course.CourseService;
import org.example.blps_lab1.core.ports.course.UserModuleProgressService;
import org.example.blps_lab1.core.ports.email.EmailService;
import org.example.blps_lab1.core.ports.sss.SimpleStorageService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class CertificateManagerImpl implements CertificateManager {
    private CertificateGenerator certificateGenerator;
    private CourseService courseService;
    private SimpleStorageService simpleStorageService;
    private EmailService emailService;
    private CourseProgressRepository courseProgressRepository;
    private UserModuleProgressService userModuleProgressService;
    private MessageProducer messageProducer; //kafka
    private ApplicationEventPublisher publisher;

    @Override
    public void getCertificate(UserXml user, Long courseUUID) {

        publisher.publishEvent(new CourseCompletedEvent(2L, UUID.randomUUID()));
//        publisher.publishEvent(new CourseCompletedEvent(user.getId(), courseUUID));

//        var course = courseService.getCourseByID(courseUUID);
//
//        boolean allModulesCompleted = course.getModules().stream()
//                .allMatch(module -> userModuleProgressService.isModuleCompletedForUser(user, module));
//
//        CourseProgress courseProgress = courseProgressRepository.findByUserEmailAndCourse(user.getUsername(), course)
//                .orElse(new CourseProgress(new CourseProgressId(course.getCourseId(), user.getId()), course, user.getUsername(), 0));
//
//        courseProgressRepository.save(courseProgress);
//
//        try {
//            var certificatePdf = certificateGenerator.generateCertificate(course.getCourseName(), user.getUsername(), null);
//            saveToSimpleStorageService(user, course, certificatePdf);
//            var userToSend = new User(user.getUsername(),user.getUsername(), user.getPassword());
//            messageProducer.sendMessage("topic", userToSend);
//
//            emailService.sendCertificateToUser(user.getUsername(), certificatePdf);
//        } catch (Exception e) {
//            log.error("Error while creating the certificate", e);
//            sendAboutException(user.getUsername());
//        }
    }

    private void saveToSimpleStorageService(UserXml user, Course course, File file) {
        StringBuilder filename = new StringBuilder();
        filename.append(user.getUsername()).append(course.getCourseName());
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
