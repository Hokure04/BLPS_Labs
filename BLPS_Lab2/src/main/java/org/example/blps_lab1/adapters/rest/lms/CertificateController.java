package org.example.blps_lab1.adapters.rest.lms;

import lombok.AllArgsConstructor;

import org.example.blps_lab1.core.ports.admin.AuthService;
import org.example.blps_lab1.lms.service.CertificateManagerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/certificate")
@AllArgsConstructor
public class CertificateController {

    private AuthService authService;
    private CertificateManagerService certificateManagerService;
    

    @GetMapping("/{courseUUID}")
    public void getMethodName(@PathVariable UUID courseUUID) {
        certificateManagerService.getCertificate(authService.getCurrentUser(), courseUUID);
    }
}
