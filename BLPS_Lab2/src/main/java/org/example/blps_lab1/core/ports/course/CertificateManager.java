package org.example.blps_lab1.core.ports.course;

import org.example.blps_lab1.core.domain.auth.User;
import org.example.blps_lab1.core.domain.course.Course;

import java.io.File;
import java.util.UUID;

public interface CertificateManager {
    void getCertificate(User user, UUID courseUUID);
}
