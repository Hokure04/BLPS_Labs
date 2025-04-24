package org.example.blps_lab1.authorization.controller;


import org.example.blps_lab1.authorization.dto.ApplicationResponseDto;

import org.example.blps_lab1.authorization.dto.JwtAuthenticationResponse;

import org.example.blps_lab1.authorization.dto.LoginRequest;
import org.example.blps_lab1.authorization.dto.RegistrationRequestDto;
import org.example.blps_lab1.authorization.service.AuthService;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import java.util.UUID;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    private AuthService authService;

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody RegistrationRequestDto request){
        return authService.signUp(request);
    }

    @PostMapping("/sign-up/{courseUUID}")
    public ApplicationResponseDto signUp(@RequestBody RegistrationRequestDto request, @PathVariable UUID courseUUID){
        return authService.signUp(request, courseUUID);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody LoginRequest request){
        return authService.signIn(request);
    }
    
}
