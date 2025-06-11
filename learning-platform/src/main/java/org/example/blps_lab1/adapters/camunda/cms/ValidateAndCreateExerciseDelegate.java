package org.example.blps_lab1.adapters.camunda.cms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.adapters.course.dto.nw.NewExerciseDto;
import org.example.blps_lab1.core.ports.course.nw.NewExerciseService;
import org.springframework.stereotype.Service;

@Service("validateAndCreateExercise")
@Slf4j
@AllArgsConstructor
public class ValidateAndCreateExerciseDelegate implements JavaDelegate {


    private final NewExerciseService newExerciseService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            String exerciseName = CamundaUtils.getVariableString(execution, "exerciseName");
            String exerciseDescription = CamundaUtils.getVariableString(execution, "exerciseDescription");
            String exerciseAnswer = CamundaUtils.getVariableString(execution, "exerciseAnswer");
            Long exercisePoints = CamundaUtils.getVariableOrNull(execution, "exercisePoints");

            if (exerciseName == null || exerciseDescription == null || exerciseAnswer == null || exercisePoints == null) {
                throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "все поля, кроме ошибки, обязательны");
            }

            var exerciseDto = NewExerciseDto
                    .builder()
                    .name(exerciseName)
                    .description(exerciseDescription)
                    .answer(exerciseAnswer)
                    .points(exercisePoints.intValue())
                    .build();
            try {
                newExerciseService.createNewExercise(exerciseDto);
                log.info("successfully created new exercise");
            } catch (Exception e) {
                log.error("failed to create new exercise: {}", e.getMessage());
                throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "не все поля заполеннны корректно");
            }
        } catch (BpmnError e) {
            throw e;
        } catch (Exception e) {
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), e.getMessage());
        }
    }
}
