package org.example.blps_lab1.adapters.camunda.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.core.domain.auth.User;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("auth")
@AllArgsConstructor
@Slf4j
public class CasualAuth implements JavaDelegate {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final IdentityService identityService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            CamundaUtils.prepareVariables(execution);
            var camundaUser = identityService.getCurrentAuthentication();
            String username = CamundaUtils.getVariableString(execution, "username");
            String password = CamundaUtils.getVariableString(execution, "password");
            User userEntity;
            try {
                userEntity = userService.getUserByEmail(username);
            } catch (UsernameNotFoundException e) {
                log.error("User not found: {}", username);
                throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "user not found");
            }
            if (!passwordEncoder.matches(password, userEntity.getPassword())) {
                log.error("Incorrect password");
                throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "password incorrect");
            }

            if (!userEntity.getCamundaUserID().equals(camundaUser.getUserId())) {
                log.error("User {} is not a camunda user, require {}", camundaUser.getUserId(), userEntity.getCamundaUserID());
                throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), "this user is not the current user of camunda");
            }

        } catch (BpmnError e) {
            throw e;
        } catch (Exception e) {
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), e.getMessage());
        }
    }
}
