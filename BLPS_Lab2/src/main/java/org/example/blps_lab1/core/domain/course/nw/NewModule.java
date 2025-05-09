package org.example.blps_lab1.core.domain.course.nw;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NewModule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    // чтобы модуль считался пройденным, должно быть >70% * totalPoints
    private Integer totalPoints;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "exercises_uuid")
    private NewExercise exercises;


    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }
}
