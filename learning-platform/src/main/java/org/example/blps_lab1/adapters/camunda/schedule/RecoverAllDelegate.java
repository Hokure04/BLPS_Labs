package org.example.blps_lab1.adapters.camunda.schedule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.miniservice.service.UserFailureService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service("recoverAll")
public class RecoverAllDelegate implements JavaDelegate {

    private final UserFailureService userFailureService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("start to recover all");
        userFailureService.recoverAll();
        log.info("finish to recover all");
    }
}
