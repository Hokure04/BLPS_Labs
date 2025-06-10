package org.example.blps_lab1.adapters.camunda.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.core.domain.auth.ApplicationStatus;
import org.example.blps_lab1.core.ports.auth.ApplicationService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("processApplicationAnswer")
@AllArgsConstructor
@Slf4j
public class ProcessApplicationAnswerDelegate implements JavaDelegate {

    private final ApplicationService applicationService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        var username = CamundaUtils.getVariableString(execution, "username");

        System.out.println("status= " + CamundaUtils.getVariableString(execution, "status"));
        ApplicationStatus userStatus = ApplicationStatus.valueOf(CamundaUtils.getVariableString(execution, "status"));
        Long chosenApplicationID = Long.parseLong(Objects.requireNonNull(CamundaUtils.getVariableString(execution, "chosenApplication")));

        try {
            log.info("chosenApplicationID={} " , chosenApplicationID);
            applicationService.updateStatus(chosenApplicationID, userStatus);
        } catch (Exception e) {
            log.error("fail to update application status", e);
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), e.getMessage());
        }
    }
}
