package org.example.blps_lab1.adapters.course.service;

import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.core.domain.auth.User;
import org.example.blps_lab1.adapters.db.auth.UserRepository;
import org.example.blps_lab1.core.exception.course.CourseNotExistException;
import org.example.blps_lab1.core.exception.common.ObjectNotExistException;
import org.example.blps_lab1.core.exception.common.ObjectNotFoundException;
import org.example.blps_lab1.adapters.course.dto.CourseDto;
import org.example.blps_lab1.core.domain.course.Course;
import org.example.blps_lab1.adapters.db.course.CourseRepository;
import org.example.blps_lab1.core.ports.course.CourseService;
import org.example.blps_lab1.core.ports.email.EmailService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.*;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TransactionTemplate transactionTemplate;
    private final DataSourceTransactionManager transactionManager;
    private final PlatformTransactionManager platformTransactionManager;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository,
                             EmailService emailService, PlatformTransactionManager platformTransactionManager, DataSourceTransactionManager transactionManager) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
        this.transactionManager = transactionManager;
        this.platformTransactionManager = platformTransactionManager;
    }

    public Course createCourse(final Course course) {
        Course newCourse = courseRepository.save(course);
        log.info("Created course: {}", newCourse);
        return newCourse;
    }

    public Course find(final UUID id) {
        return courseRepository.findById(id).orElseThrow(() -> new ObjectNotExistException("Курс с id: " + id + " не существует"));
    }

    public List<Course> addAll(List<Course> courses) {
        return courseRepository.saveAll(courses);
    }


    public Course getCourseByUUID(final UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ObjectNotExistException("Курс с таким id не существует"));
    }

    public void deleteCourse(final UUID courseUUID) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("deleteCourse");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            courseRepository.findById(courseUUID).orElseThrow(() -> new CourseNotExistException("Курс с таким id не существует"));
            courseRepository.deleteById(courseUUID);
            log.info("Course deleted: {}", courseUUID);
            transactionManager.commit(status);
        }catch (Exception e){
            transactionManager.rollback(status);
            throw new RuntimeException("Откат");
        }
    }

    public List<Course> getAllCourses() {
        var list = courseRepository.findAll();
        log.info("Get courses list {}", list.size());
        return list;
    }

    public Course updateCourse(UUID courseUUID, CourseDto courseDto) {
        return transactionTemplate.execute(status -> {
            if (courseRepository.findById(courseUUID).isEmpty()) {
                log.error("Course with id {} does not exist", courseUUID);
                throw new ObjectNotFoundException("Курс не найден");
            }
            return courseRepository.findById(courseUUID).map(course -> {
                course.setCourseName(courseDto.getCourseName());
                course.setCoursePrice(courseDto.getCoursePrice());
                course.setTopicName(courseDto.getTopicName());
                course.setCourseDuration(courseDto.getCourseDuration());
                course.setWithJobOffer(courseDto.getWithJobOffer());
                return courseRepository.save(course);
            }).orElseThrow(() -> {
                log.error("Course with id {} can't be updated", courseUUID);
                return new RuntimeException("Не получилось обновить курс");
            });
        });
    }

    public List<Course> enrollUser(Long userId, UUID courseUUID) {
        return transactionTemplate.execute(status -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("user not found in enroll"));

            Course course = courseRepository.findById(courseUUID)
                    .orElseThrow(() -> new RuntimeException("course not found in enroll"));

            List<Course> enrolledCourses = new ArrayList<>();


            if (!user.getCourseList().contains(course)) {
                user.getCourseList().add(course);
                enrolledCourses.add(course);
            }

            List<Course> additionalCourses = new ArrayList<>(course.getAdditionalCourseList());
            user.getCourseList().addAll(additionalCourses);
            emailService.informAboutNewCourses(
                    user.getEmail(),
                    course.getCourseName(),
                    course.getCoursePrice(),
                    additionalCourses);
            enrolledCourses.addAll(additionalCourses);
            userRepository.save(user);
            return enrolledCourses;
        });
    }

    /**
     * Утилитарная функция-транзакция для записи на дополнительные курсы, связанные с основными
     * например, если вы проходите курс "Бухгалтер будущего", и к этому курсу закреплен курс "Python для чайников",
     * то пользователя автоматически должно записать на курс по Python
     *
     * @param courseUUID           основного курса
     * @param additionalCourseUUID второстепенного курса
     * @return основной курс(тот, что с UUID: {@code courseUUID}
     */
    public Course addAdditionalCourses(UUID courseUUID, UUID additionalCourseUUID) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("addAdditionalCourses");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            Course course = courseRepository.findById(courseUUID)
                    .orElseThrow(() -> new ObjectNotFoundException("Курс с id " + courseUUID + " не найден"));

            Course additionalCourse = courseRepository.findById(additionalCourseUUID)
                    .orElseThrow(() -> new ObjectNotFoundException("Дополнительный курс с id " + additionalCourseUUID + " не найден"));

            if (!course.getAdditionalCourseList().contains(additionalCourse)) {
                course.getAdditionalCourseList().add(additionalCourse);
                courseRepository.save(course);
                log.info("Курс {} добавлен в дополнительные курсы для {}", additionalCourseUUID, courseUUID);
            } else {
                log.warn("Курс {} уже есть в дополнительных курсах для {}", additionalCourseUUID, courseUUID);
            }
            transactionManager.commit(status);
            return course;
        }catch (Exception e){
            transactionManager.rollback(status);
            throw new RuntimeException(e);
        }
    }

    public Course addListOfCourses(UUID uuid, List<Course> additionalCourses) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("addListOfCourses");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            Course course = courseRepository.findById(uuid)
                    .orElseThrow(() -> new ObjectNotFoundException("Курс с uuid " + uuid + " не найден"));

            for (Course additionalCourse : additionalCourses) {
                if (additionalCourse.getCourseUUID() == null) {
                    courseRepository.save(additionalCourse);
                }
            }
            course.getAdditionalCourseList().addAll(additionalCourses);
            courseRepository.save(course);
            log.info("Курсы добавлены в дополнительные курсы для курса с uuid {}", uuid);
            transactionManager.commit(status);
            return course;
        }catch (Exception e){
            transactionManager.rollback(status);
            throw new RuntimeException(e);
        }
    }
}
