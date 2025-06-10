package org.example.blps_lab1.adapters.camunda.util;

import lombok.Getter;

@Getter
public enum Codes {

    ERROR_MESSAGE("errorMessage");

    private final String stringName;
    Codes(String stringName) {
        this.stringName = stringName;
    }

}
