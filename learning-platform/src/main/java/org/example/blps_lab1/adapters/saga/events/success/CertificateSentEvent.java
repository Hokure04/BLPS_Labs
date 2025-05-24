package org.example.blps_lab1.adapters.saga.events.success;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.blps_lab1.core.domain.auth.UserXml;
import org.example.blps_lab1.core.domain.course.nw.NewCourse;

import java.io.File;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CertificateSentEvent {
    private UserXml user;
    private File pdf;
}
