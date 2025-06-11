package org.example.blps_lab1.adapters.camunda.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.core.ports.admin.AdminPanelService;
import org.springframework.stereotype.Service;

@Service("roleAssign")
@AllArgsConstructor
@Slf4j
public class RoleAssignDelegate implements JavaDelegate {
    private final AdminPanelService adminPanelService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            String emailToUpdate = CamundaUtils.getVariableString(execution, "alienEmail");

            try {
                adminPanelService.updateRole(emailToUpdate, "ROLE_ADMIN");
            } catch (Exception e) {
                log.error("fail to update role {}", e.getMessage());
                throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "не удалось обновить email");
            }
        } catch (BpmnError e) {
            throw e;
        } catch (Exception e) {
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), e.getMessage());
        }
    }
}
