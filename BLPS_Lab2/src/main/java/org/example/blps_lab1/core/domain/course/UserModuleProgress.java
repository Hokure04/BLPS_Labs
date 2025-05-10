//package org.example.blps_lab1.core.domain.course;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.example.blps_lab1.core.domain.auth.User;
//
//@Entity
//@Table(name = "user_module_progress")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class UserModuleProgress {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String userEmail;
//
//    @ManyToOne
//    @JoinColumn(name = "module_id", nullable = false)
//    private Module module;
//
//    @Column(nullable = false)
//    private Boolean isCompleted = false;
//
//    @Column(nullable = false)
//    private Integer points = 0;
//}
