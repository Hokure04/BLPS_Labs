package org.example.blps_lab1.configuration;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    private String username;
    private String email;
    private String password;
}
