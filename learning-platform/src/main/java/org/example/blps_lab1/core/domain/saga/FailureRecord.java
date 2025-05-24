package org.example.blps_lab1.core.domain.saga;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class FailureRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName; //NOTE: aka email in the whole system
    private String courseName;
    @Enumerated(EnumType.STRING)
    private SagaFailedStep sagaFailedStep;
    private String errorMessage;

    private Instant createdAt;
    private Instant updatedAt;


    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

}