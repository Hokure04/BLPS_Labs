package org.example.blps_lab1.core.ports.admin;

public interface UserEnrollmentService {
    void processEnrolment(Long applicationEnrollmentId, String applicationStatus);
}
