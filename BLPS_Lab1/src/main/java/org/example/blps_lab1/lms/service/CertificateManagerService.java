package org.example.blps_lab1.lms.service;

import java.io.File;

import org.example.blps_lab1.authorization.models.User;
import org.example.blps_lab1.common.service.MinioService;
import org.example.blps_lab1.courseSignUp.models.Course;
import org.example.blps_lab1.courseSignUp.service.CourseService;
import org.example.blps_lab1.export.certificate.CertificateExporter;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class CertificateManagerService {
    private CertificateExporter certificateExporter;
    private CourseService courseService;
    private MinioService minioService;
    private EmailService emailService;

    public void getCertificate(User user, Long courseId) {
        var course = courseService.getCourseById(courseId);
        var certificatePdf = certificateExporter.generateCertificate(course.getCourseName(), user.getEmail(), null);
        saveToMinio(user, course, certificatePdf);
        sendToUser(user, certificatePdf);
    }


    private void sendToUser(User user, File file){
        //TODO: нужна ручка для отправления файлов
        // emailService.createMimeMessageHelper(null, null)
    }
    private void saveToMinio(User user, Course course, File file){
        StringBuilder filename = new StringBuilder();
        filename.append(user.getEmail()).append(course.getCourseName());
        minioService.uploadFile(user.getUsername(), filename.toString(), file);
        
    }
}
