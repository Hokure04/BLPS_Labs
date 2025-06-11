package org.example.blps_lab1.adapters.camunda.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.core.domain.auth.Role;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.springframework.stereotype.Service;

@Service("validateUserRole")
@Slf4j
@AllArgsConstructor
public class ValidateUserRoleDelegate implements JavaDelegate {
    private final UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            String username = CamundaUtils.getVariableString(execution, "username");
            var user = userService.getUserByEmail(username);

            if (user == null) {
                log.error("User not found: {}", username);
                throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "Внутренняя ошибка, пользователь не найден");
            }

            if (user.getRole() != Role.ROLE_ADMIN) {
                log.error("user has {} role, require role admin", user.getRole());
                throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "У вас недостаточно прав");
            }
        } catch (BpmnError e) {
            throw e;
        } catch (Exception e) {
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), e.getMessage());
        }
    }
}
