package org.example.blps_lab1.adapters.camunda.util;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;

@Slf4j
public class CamundaUtils {
    public static <T> T getVariableOrNull(DelegateExecution execution, String variableName) {
        if (execution == null || variableName == null) {
            log.warn("empty input data for util method: getVariableOrNull");
            return null;
        }

        T result = null;

        try {
            result = (T) execution.getVariable(variableName);
        } catch (ClassCastException e) {
            log.error("Camunda variable not such type as you want to handle variable {}", variableName, e);
        }
        return result;
    }

    public static String getVariableString(DelegateExecution execution, String variableName) {
        String str = getVariableOrNull(execution, variableName);
        if (str == null || str.isEmpty() || str.equals("null") || str.equals(" ")) {
            return null;
        }
        return str;
    }


    public static void prepareVariables(DelegateExecution execution) {
        prepareErrorVariables(execution);
    }

    public static void prepareErrorVariables(DelegateExecution execution) {
        execution.setVariable(Codes.ERROR_MESSAGE.getStringName(), "");
    }

}
