package org.example.blps_lab1.adapters.camunda.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.auth.dto.RegistrationRequestDto;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.core.ports.auth.AuthService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("registrationDelegate")
@AllArgsConstructor
@Slf4j
public class RegistrationDelegate implements JavaDelegate {

    private final IdentityService identityService;
    private final AuthService authService;

    @Override
    public void execute(DelegateExecution ex) throws Exception {
        try {
            var currentUser = identityService.getCurrentAuthentication();
            String userID = currentUser.getUserId();

            String email = CamundaUtils.getVariableString(ex, "email");
            String password = CamundaUtils.getVariableString(ex, "password");
            String firstName = CamundaUtils.getVariableString(ex, "firstName");

            String lastName = CamundaUtils.getVariableString(ex, "lastName");
            String phoneNumber = CamundaUtils.getVariableString(ex, "phoneNumber");
            String courseUUID = CamundaUtils.getVariableString(ex, "courseUUID");
            var regDto = RegistrationRequestDto.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .phoneNumber(phoneNumber)
                    .email(email)
                    .password(password)
                    .userID(userID)
                    .build();
            if (courseUUID == null) {
                authService.signUp(regDto);
            } else {
                authService.signUp(regDto, UUID.fromString(courseUUID));
            }
        }catch (BpmnError e) {
            throw e;
        }catch (Exception e) {
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), e.getMessage());
        }
    }
}
