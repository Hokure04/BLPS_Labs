package org.example.blps_lab1.adapters.camunda.cms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.adapters.course.dto.nw.NewCourseDto;
import org.example.blps_lab1.core.domain.course.Topic;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("validateAndCreateCourse")
@Slf4j
@AllArgsConstructor
public class ValidateAndCreateCourseDelegate implements JavaDelegate {


    private final NewCourseService newCourseService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String courseName = CamundaUtils.getVariableString(execution, "courseName");
        String courseDescription = CamundaUtils.getVariableString(execution, "courseDescription");
        Long coursePrice = CamundaUtils.getVariableOrNull(execution, "coursePrice");
        String topic = CamundaUtils.getVariableString(execution, "topic");


        if (courseName == null || courseDescription == null || coursePrice == null || topic == null) {
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "все поля, кроме ошибки, обязательны");
        }

        var courseDto = NewCourseDto
                .builder()
                .name(courseName)
                .description(courseDescription)
                .price(new BigDecimal(coursePrice))
                .topic(Topic.valueOf(topic))
                .build();
        try {
            newCourseService.createCourse(courseDto);
            log.info("successfully created new course");
        } catch (Exception e) {
            log.error("failed to create new course: {}", e.getMessage());
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "не все поля заполеннны корректно");
        }
    }
}
