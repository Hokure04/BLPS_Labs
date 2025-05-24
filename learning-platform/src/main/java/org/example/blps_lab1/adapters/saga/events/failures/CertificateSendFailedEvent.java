package org.example.blps_lab1.adapters.saga.events.failures;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.blps_lab1.core.domain.auth.UserXml;
import org.example.blps_lab1.core.domain.course.nw.NewCourse;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CertificateSendFailedEvent {
    private UserXml user;
    private NewCourse course;
    private Exception exception;
}
