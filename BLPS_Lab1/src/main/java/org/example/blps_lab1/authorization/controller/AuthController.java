package org.example.blps_lab1.authorization.controller;

import org.apache.commons.lang3.NotImplementedException;
import org.example.blps_lab1.authorization.dto.ApplicationResponseDto;
import org.example.blps_lab1.authorization.dto.LoginRequest;
import org.example.blps_lab1.authorization.dto.RegistrationRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    @PostMapping("/sign-up")
    public ApplicationResponseDto signUp(@RequestBody RegistrationRequestDto request){
        throw new NotImplementedException("Need to request to courses db");
    }

    @PostMapping("/sign-in")
    public ApplicationResponseDto signIn(@RequestBody LoginRequest request){
        throw new NotImplementedException();
    }

}
