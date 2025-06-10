package org.example.blps_lab1.adapters.camunda.lms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.springframework.stereotype.Service;

@Service("getCourses")
@Slf4j
@AllArgsConstructor
public class GetCoursesDelegate implements JavaDelegate {

    private final NewCourseService newCourseService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

    }
}
