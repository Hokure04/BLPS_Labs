package org.example.blps_lab1.adapters.camunda.lms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.adapters.saga.events.success.CourseCompletedEvent;
import org.example.blps_lab1.core.exception.course.InvalidFieldException;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service("validateFinishingCourse")
@Slf4j
@AllArgsConstructor
public class ValidateFinishingCourseDelegate implements JavaDelegate {
    private final NewCourseService newCourseService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        UUID chosenCourseUUID = UUID.fromString(Objects.requireNonNull(CamundaUtils.getVariableString(execution, "chosenCourse")));

        boolean allModulesCompleted = newCourseService.isCourseFinished(chosenCourseUUID);
        if (!allModulesCompleted) {
            execution.setVariable("isCourseFinished", false);
        }
        execution.setVariable("isCourseFinished", true);
    }
}
