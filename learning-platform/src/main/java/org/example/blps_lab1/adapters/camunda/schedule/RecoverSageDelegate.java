package org.example.blps_lab1.adapters.camunda.schedule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.saga.RecoveryService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service("recoverSage")
public class RecoverSageDelegate implements JavaDelegate {
    private final RecoveryService recoveryService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("RecoverSageDelegate starting");
        recoveryService.recoverFileUploads();
        log.info("RecoverSageDelegate finished");
    }
}
