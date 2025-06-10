package org.example.blps_lab1.adapters.camunda.lms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service("getModules")
@Slf4j
@AllArgsConstructor
public class GetModulesDelegate implements JavaDelegate {

    private final UserService userService;
    private final NewCourseService newCourseService;


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String chosenCourse = CamundaUtils.getVariableString(execution, "chosenCourse");
        if (chosenCourse == null || chosenCourse.isEmpty()) {
            log.warn("No chosen course found");
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "Курс не выбран");
        }
        var course = newCourseService.find(UUID.fromString(chosenCourse));
        var moduleList = course.getNewModuleList();
        log.info("Found {} new modules", moduleList.size());
        execution.setVariable("moduleList", moduleList.toString());
    }
}
