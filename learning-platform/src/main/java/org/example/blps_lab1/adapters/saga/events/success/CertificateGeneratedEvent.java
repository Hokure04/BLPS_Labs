package org.example.blps_lab1.adapters.saga.events.success;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.blps_lab1.core.domain.auth.User;
import org.example.blps_lab1.core.domain.auth.UserXml;
import org.example.blps_lab1.core.domain.course.nw.NewCourse;

import java.io.File;

@Data
@AllArgsConstructor
public class CertificateGeneratedEvent {
    private User user;
    private NewCourse course;
    private File pdf;
}
