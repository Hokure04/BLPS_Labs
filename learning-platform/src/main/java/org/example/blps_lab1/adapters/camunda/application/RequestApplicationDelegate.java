package org.example.blps_lab1.adapters.camunda.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.CamundaUtils;
import org.example.blps_lab1.core.domain.auth.Application;
import org.example.blps_lab1.core.domain.auth.User;
import org.example.blps_lab1.core.ports.auth.ApplicationService;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("requestApplication")
@AllArgsConstructor
@Slf4j
public class RequestApplicationDelegate implements JavaDelegate {

    private final UserService userService;
    private final ApplicationService applicationService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        User currentUser = userService.getUserByEmail(CamundaUtils.getVariableString(execution,"username"));

        List<Application> applicationList = applicationService.getByUserId(currentUser.getId());
        execution.setVariable("applicationList", applicationList.toString());
    }
}
