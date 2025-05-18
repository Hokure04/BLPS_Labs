package org.example.blps_lab1.adapters.course.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.core.domain.course.Course;
import org.example.blps_lab1.core.domain.course.CourseProgress;
import org.example.blps_lab1.core.domain.course.CourseProgressId;
import org.example.blps_lab1.adapters.db.course.CourseProgressRepository;
import org.example.blps_lab1.adapters.db.course.CourseRepository;
import org.example.blps_lab1.core.ports.course.CourseProgressService;
import org.example.blps_lab1.core.ports.db.UserDatabase;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Slf4j
public class CourseProgressServiceImpl implements CourseProgressService {

    private final CourseRepository courseRepository;
    private final CourseProgressRepository courseProgressRepository;
    private final UserDatabase userRepository;
    private final TransactionTemplate transactionTemplate;

    public CourseProgressServiceImpl(CourseRepository courseRepository,
                                     CourseProgressRepository courseProgressRepository,
                                     UserDatabase userRepository,
                                     PlatformTransactionManager platformTransactionManager) {
        this.courseRepository = courseRepository;
        this.courseProgressRepository = courseProgressRepository;
        this.userRepository = userRepository;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
    }

    @Override
    public void addPoints(Long userId, Long courseUUID, int points) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NotNull TransactionStatus status) {
                CourseProgressId courseProgressId = new CourseProgressId(courseUUID, userId);
                CourseProgress progress = courseProgressRepository.findByCourseProgressId(courseProgressId)
                        .orElseGet(() -> {
                            Course course = courseRepository.findById(courseUUID)
                                    .orElseThrow(() -> new EntityNotFoundException("Курс не найден addpoints"));
                            var user = userRepository.findById(userId)
                                    .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден в addPoints"));
                            CourseProgress newProgress = new CourseProgress();
                            newProgress.setCourseProgressId(courseProgressId);
                            newProgress.setCourse(course);
                            newProgress.setUserEmail(user.getUsername());
                            newProgress.setEarnedPoints(0);
                            return newProgress;
                        });

                progress.setEarnedPoints(progress.getEarnedPoints() + points);
                courseProgressRepository.save(progress);
                log.info("User {} earned {} points for course {}", userId, points, courseUUID);
            }
        });
    }
}

