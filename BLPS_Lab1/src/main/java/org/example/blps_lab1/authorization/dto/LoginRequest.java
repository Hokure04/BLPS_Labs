package org.example.blps_lab1.authorization.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    private String companyName;//NOTE: nullable field, validating only if client specified 
}
