package org.example.blps_lab1.adapters.camunda.util;

public enum Codes {

    ERROR_MESSAGE("errorMessage");

    private final String stringName;
    Codes(String stringName) {
        this.stringName = stringName;
    }

    public String getStringName() {
        return stringName;
    }
}
