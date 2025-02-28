package org.example.blps_lab1.lms.controller;

import lombok.AllArgsConstructor;

import org.example.blps_lab1.authorization.service.AuthService;
import org.example.blps_lab1.lms.service.CertificateManagerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/certificate")
@AllArgsConstructor
public class CertificateController {

    private AuthService authService;
    private CertificateManagerService certificateManagerService;
    

    @GetMapping("/certificate/{courseId}")
    public void getMethodName(@RequestParam Long course_id) {
        certificateManagerService.getCertificate(authService.getCurrentUser(), course_id);
    }
}
