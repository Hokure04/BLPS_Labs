package org.example.blps_lab1.core.domain.course.nw;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "new_exercise_id")
    private NewExercise finishedExercise;

    // должно было бы быть @ManyToOne на User -> пользователи существуют только в xml
    //  поэтому есть потребность указывать идюк UserXml в виде обычной записи
    private Long user_id;

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "new_course_uuid"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<NewCourse> courses = new ArrayList<>();
}
