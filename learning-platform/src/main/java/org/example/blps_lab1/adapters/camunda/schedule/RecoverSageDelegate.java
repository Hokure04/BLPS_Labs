package org.example.blps_lab1.adapters.camunda.schedule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.example.blps_lab1.adapters.camunda.util.Codes;
import org.example.blps_lab1.adapters.saga.RecoveryService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service("recoverSage")
public class RecoverSageDelegate implements JavaDelegate {
    private final RecoveryService recoveryService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            log.info("RecoverSageDelegate starting");
            recoveryService.recoverFileUploads();
            log.info("RecoverSageDelegate finished");
        } catch (BpmnError e) {
            throw e;
        } catch (Exception e) {
            throw new BpmnError(Codes.ERROR_MESSAGE.getStringName(), e.getMessage());
        }
    }
}
