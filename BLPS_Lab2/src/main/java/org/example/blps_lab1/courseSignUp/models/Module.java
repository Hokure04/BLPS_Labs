package org.example.blps_lab1.courseSignUp.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.blps_lab1.authorization.models.User;
import org.example.blps_lab1.courseSignUp.repository.UserModuleProgressRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "module")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private Boolean isCompleted;

    @Column(nullable = false)
    private Integer orderNumber;

    @Column
    private String description;

    @Column
    private Boolean isBlocked;

    @Column
    private Integer totalPoints;

    @Column
    private LocalDateTime localDateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ModuleExercise> moduleExercises;

}
