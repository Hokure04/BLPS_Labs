package org.example.blps_lab1.adapters.camunda.lms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.core.domain.course.nw.NewCourse;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("getCourses")
@Slf4j
@AllArgsConstructor
public class GetCoursesDelegate implements JavaDelegate {

    private final NewCourseService newCourseService;
    private final UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            String username = (String) execution.getVariable("username");
            var user = userService.getUserByEmail(username);

            List<NewCourse> courseList = newCourseService.getCourseByUserID(user.getId());
            execution.setVariable("courseList", courseList.toString());
            log.info("set course list with size {}, for user {}", courseList.size(), user);
        } catch (BpmnError e) {
            throw e;
        } catch (Exception e) {
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), e.getMessage());
        }
    }
}
