package org.example.blps_lab1.adapters.camunda.lms;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.adapters.db.course.StudentRepository;
import org.example.blps_lab1.core.domain.course.nw.Student;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.example.blps_lab1.core.ports.course.nw.NewExerciseService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service("validateAns")
@AllArgsConstructor
@Slf4j
public class ValidateExerciseDelegate implements JavaDelegate {

    private final NewExerciseService newExerciseService;
    private final StudentRepository studentRepository;
    private final UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String userAns = CamundaUtils.getVariableString(execution, "exerciseAns");
        String exerciseUUID = CamundaUtils.getVariableString(execution, "chosenExercise");
        String username = CamundaUtils.getVariableString(execution, "username");

        if (exerciseUUID == null || userAns == null || username == null) {
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "Не все обязательные поля заполнены");
        }

        var user = userService.getUserByEmail(username);
        if (user == null) {
            log.error("fail to find user by email: {}", username);
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "Внутренняя ошибка: попробуйте позже, пользователь не найден");
        }
        Optional<Student> optionalStudent = studentRepository.findByUser_Id(user.getId());
        if (optionalStudent.isEmpty()) {
            log.error("fail to find student by usid: {}", user.getId());
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "Внутреняя ошибка: студент не найден");
        }
        var student = optionalStudent.get();

        var exercise = newExerciseService.find(UUID.fromString(exerciseUUID));
        if (exercise.getAnswer().equals(userAns)) {
            student.getFinishedExercises().add(exercise); // todo проверить, сохраняет ли он аналогично submitAns
            log.info("course finished successfully");
        }else {
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "неверный ответ");
        }
    }
}
