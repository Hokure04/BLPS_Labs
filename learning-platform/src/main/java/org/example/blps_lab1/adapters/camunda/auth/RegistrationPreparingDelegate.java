package org.example.blps_lab1.adapters.camunda.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.springframework.stereotype.Service;

@Service("registrationPreparing")
@AllArgsConstructor
@Slf4j
public class RegistrationPreparingDelegate implements JavaDelegate {

    private final NewCourseService newCourseService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        CamundaUtils.prepareVariables(execution);
        Boolean isCourseNeeded = CamundaUtils.getVariableOrNull(execution, "isCoursesNeeded");

        if(Boolean.TRUE.equals(isCourseNeeded)) {
            var courses = newCourseService.getAllCourses().toString();
            execution.setVariable("listCourses", courses);
        }
    }
}
