//package org.example.blps_lab1.adapters.db.course;
//
//import org.example.blps_lab1.core.domain.course.Course;
//import org.example.blps_lab1.core.domain.course.Module;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface ModuleRepository extends JpaRepository<Module, Long> {
//    Optional<Module> findByCourseAndOrderNumber(Course course, Integer orderNumber);
//    List<Module> findByCourseOrderByOrderNumberAsc(Course course);
//
//    List<Module> findAllByCourse_CourseId(Long courseCourseId);
//}
