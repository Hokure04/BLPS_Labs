package org.example.blps_lab1.adapters.saga.events.success;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CertificateGeneratedEvent {
    private Long userId;
    private UUID courseId;
    private File pdf;
}
