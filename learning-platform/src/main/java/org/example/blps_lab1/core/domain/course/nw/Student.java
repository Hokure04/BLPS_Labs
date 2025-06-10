package org.example.blps_lab1.core.domain.course.nw;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.blps_lab1.core.domain.auth.User;

import java.util.ArrayList;
import java.util.List;


@Table(name = "student")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "finished_exercise",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "new_exercise_uuid")
    )
    private List<NewExercise> finishedExercises = new ArrayList<>();

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "new_course_uuid")
    )
    private List<NewCourse> courses = new ArrayList<>();
}
