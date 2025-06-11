package org.example.blps_lab1.adapters.miniservice.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MiniUser {
    private String username;
    private String email;
    private String password;
}
