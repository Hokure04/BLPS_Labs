package org.example.blps_lab1.adapters.camunda.lms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.core.ports.course.nw.NewModuleService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("getExercise")
@AllArgsConstructor
@Slf4j
public class GetExerciseDelegate implements JavaDelegate {

    private final NewModuleService newModuleService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            String chosenModule = CamundaUtils.getVariableString(execution, "chosenModule");

            if (chosenModule == null || chosenModule.isEmpty()) {
                log.warn("user didn't specify chosenModule field");
                throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "Модуль не выбран");
            }
            var module = newModuleService.getModuleByUUID(UUID.fromString(chosenModule));
            log.info("Found new module {}", module);

            execution.setVariable("exerciseList", module.getExercises().toString());
        } catch (BpmnError e) {
            throw e;
        } catch (Exception e) {
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), e.getMessage());
        }
    }
}
