package org.example.blps_lab1.adapters.course.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.core.domain.auth.User;
import org.example.blps_lab1.adapters.db.auth.UserRepository;
import org.example.blps_lab1.core.domain.course.Course;
import org.example.blps_lab1.core.domain.course.CourseProgress;
import org.example.blps_lab1.core.domain.course.CourseProgressId;
import org.example.blps_lab1.adapters.db.course.CourseProgressRepository;
import org.example.blps_lab1.adapters.db.course.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CourseProgressService {

    private final CourseRepository courseRepository;
    private final CourseProgressRepository courseProgressRepository;
    private final UserRepository userRepository;

    public void addPoints(Long userId, UUID courseUUID, int points){
        CourseProgressId courseProgressId = new CourseProgressId(courseUUID, userId);
        CourseProgress progress = courseProgressRepository.findByCourseProgressId(courseProgressId)
                .orElseGet(() -> {
                    Course course = courseRepository.findById(courseUUID)
                            .orElseThrow(() -> new EntityNotFoundException("Курс не найден addpoints"));
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден в addPoints"));
                    CourseProgress newProgress = new CourseProgress();
                    newProgress.setCourseProgressId(courseProgressId);
                    newProgress.setCourse(course);
                    newProgress.setUser(user);
                    newProgress.setEarnedPoints(0);
                    return newProgress;
                });

        progress.setEarnedPoints(progress.getEarnedPoints() + points);
        courseProgressRepository.save(progress);
        log.info("User {} earned {} points for course {}", userId, points, courseUUID);
    }
}

