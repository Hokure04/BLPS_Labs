package org.example.blps_lab1.adapters.db.course;

import org.example.blps_lab1.core.domain.course.nw.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
