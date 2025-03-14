package org.example.blps_lab1.authorization.service.impl;

import org.example.blps_lab1.authorization.models.Application;
import org.example.blps_lab1.authorization.models.ApplicationStatus;
import org.example.blps_lab1.authorization.repository.ApplicationRepository;
import org.example.blps_lab1.authorization.service.AuthService;
import org.example.blps_lab1.authorization.service.UserService;

import org.example.blps_lab1.common.exceptions.ObjectNotFoundException;
import org.example.blps_lab1.courseSignUp.service.CourseService;
import org.example.blps_lab1.lms.service.EmailService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @Slf4j @AllArgsConstructor @Transactional
public class UserEnrollmentService {
    private ApplicationService applicationService;
    private UserService userService;
    private AuthService authService;
    private CourseService courseService;
    private EmailService emailService;
    private ApplicationRepository applicationRepository;

    public void processEnrolment(Long applicationEnrollmentId, ApplicationStatus status){
        var applicationEntity = applicationService.updateStatus(applicationEnrollmentId, status);
        Application application = applicationRepository.findById(applicationEnrollmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Зявка с даным id "+ applicationEnrollmentId + "не найдена"));
        if(application.getStatus() != ApplicationStatus.PENDING){
            throw new IllegalStateException("Нельзя изменить статус уже сформированной заявки");
        }

        if (status == ApplicationStatus.REJECT){
            emailService.rejectionMail(authService.getCurrentUser().getEmail(), applicationEntity.getCourse().getCourseName());
            return;
        }

        var user = authService.getCurrentUser();
        userService.enrollUser(user, applicationEntity.getCourse());
        courseService.enrollUser(user.getId(), applicationEnrollmentId);
    }

}

