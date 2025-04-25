package org.example.blps_lab1.core.admin;

import org.example.blps_lab1.core.domain.Application;
import org.example.blps_lab1.core.domain.ApplicationStatus;
import org.example.blps_lab1.core.domain.User;

import java.util.UUID;

public interface ApplicationService {
    Application add(UUID courseUUID);
    Application add(UUID courseUUID, User user);

    Application updateStatus(Long id, ApplicationStatus applicationStatus);

}
