package org.example.blps_lab1.adapters.saga;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class CourseCompletedEvent {
    private final Long userId;
    private final UUID courseId;
}
