package org.example.blps_lab1.courseSignUp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Exercise extends JpaRepository<Exercise, Long> {
}
