package org.example.blps_lab1.adapters.saga.events.failures;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CertificateGenerationFailedEvent {
    private Long userId;
    private UUID courseId;
    private Exception exception;
}
