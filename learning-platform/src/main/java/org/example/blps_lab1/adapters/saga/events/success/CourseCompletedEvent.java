package org.example.blps_lab1.adapters.saga.events.success;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class CourseCompletedEvent {
    private Long userId;
    private UUID courseId;
}
