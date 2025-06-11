package org.example.blps_lab1.adapters.camunda.cms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.adapters.course.dto.nw.NewModuleDto;
import org.example.blps_lab1.core.ports.course.nw.NewModuleService;
import org.springframework.stereotype.Service;

@Service("validateAndCreateModule")
@Slf4j
@AllArgsConstructor
public class ValidateAndCreateModuleDelegate implements JavaDelegate {


    private final NewModuleService newModuleService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String exerciseName = CamundaUtils.getVariableString(execution, "moduleName");
        String exerciseDescription = CamundaUtils.getVariableString(execution, "moduleDescription");

        if (exerciseName == null || exerciseDescription == null) {
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "все поля, кроме ошибки, обязательны");
        }

        var moduleDto = NewModuleDto
                .builder()
                .name(exerciseName)
                .description(exerciseDescription)
                .build();
        try {
            newModuleService.createModule(moduleDto);
            log.info("successfully created new module");
        } catch (Exception e) {
            log.error("failed to create new module: {}", e.getMessage());
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "не все поля заполеннны корректно");
        }
    }
}
