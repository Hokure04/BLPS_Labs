package org.example.blps_lab1.core.domain.course;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "old_exercise")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String answer;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @Column
    private LocalDateTime localDateTime;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ModuleExercise> moduleExercises;

    public int getPointsForDifficulty(){
        return switch (difficultyLevel){
            case HARD -> 25;
            case MEDIUM -> 10;
            case EASY -> 5;
        };
    }
}
