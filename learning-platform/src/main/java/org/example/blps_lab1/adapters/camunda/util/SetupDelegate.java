package org.example.blps_lab1.adapters.camunda.util;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("preparing")
public class SetupDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        CamundaUtils.prepareVariables(execution);
    }

}
