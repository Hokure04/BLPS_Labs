//package org.example.blps_lab1.core.domain.course;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.example.blps_lab1.core.domain.auth.User;
//
//@Entity
//@Data
//@Table(name = "course_progress")
//@NoArgsConstructor
//@AllArgsConstructor
//@Deprecated(forRemoval = true)
//public class CourseProgress {
//
//    @EmbeddedId
//    private CourseProgressId courseProgressId;
//
//    @ManyToOne
//    @MapsId("courseId")
//    @JoinColumn(name = "course_id")
//    private NewCourse course;
//
//    private String userEmail;
//
//    @Column(nullable = false)
//    private Integer earnedPoints;
//}
