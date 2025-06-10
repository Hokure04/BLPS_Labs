package org.example.blps_lab1.adapters.camunda.lms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.adapters.saga.events.success.CourseCompletedEvent;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service("startSending")
@AllArgsConstructor
@Slf4j
public class StartSendingCertificateDelegate implements JavaDelegate {

    private final UserService userService;
    private final NewCourseService newCourseService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String username = CamundaUtils.getVariableString(execution, "username");
        UUID chosenCourseUUID = UUID.fromString(Objects.requireNonNull(CamundaUtils.getVariableString(execution, "chosenCourse")));
        var user = userService.getUserByEmail(username);
        var courseEntity = newCourseService.find(chosenCourseUUID);

        log.info("start sending certificate for user {}", username);
        applicationEventPublisher.publishEvent(new CourseCompletedEvent(user, courseEntity));
    }
}
