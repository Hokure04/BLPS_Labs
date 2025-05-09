package org.example.blps_lab1.core.domain.course.nw;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.blps_lab1.core.domain.course.DifficultyLevel;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;


    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String answer;


    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @Column
    private LocalDateTime createdAt;

    //todo
//    public int getPointsForDifficulty(){
//        return switch (difficultyLevel){
//            case HARD -> 25;
//            case MEDIUM -> 10;
//            case EASY -> 5;
//        };
//    }

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }
}