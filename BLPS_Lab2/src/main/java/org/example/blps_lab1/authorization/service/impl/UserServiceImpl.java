package org.example.blps_lab1.authorization.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.blps_lab1.authorization.models.User;
import org.example.blps_lab1.authorization.repository.UserRepository;
import org.example.blps_lab1.authorization.service.UserService;
import org.example.blps_lab1.common.exceptions.ObjectNotExistException;
import org.example.blps_lab1.courseSignUp.models.Course;
import org.example.blps_lab1.courseSignUp.service.CourseService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CourseService courseService;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CourseService courseService, PlatformTransactionManager transactionManager) {
        this.userRepository = userRepository;
        this.courseService = courseService;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public User add(final User user) {
//        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //transactionTemplate.execute(status -> {
        user.setPassword(user.getPassword());
        User savedUser = userRepository.save(user);
        log.info("{} registered successfully", user.getUsername());
        return savedUser;
//        });
//        return newUser;
    }

    @Override
    public List<User> addAll(final List<User> users) {
        return userRepository.saveAll(users);
    }

    @Override
    public User updateUser(final User user) {
        User newUser = userRepository.save(user);
        log.info("{} updated successfully", user.getUsername());
        return newUser;
    }


    @Override
    public boolean isExist(final String email) {
        Optional<User> potentialUser = userRepository.findByEmail(email);
        if (potentialUser.isPresent()) {
            log.info("User with username: {} exist", email);
            return true;
        }
        log.info("User with username: {} not exist", email);
        return false;
    }

    @Override
    public User getUserByEmail(final String email) {
        if (!isExist(email)) {
            throw new UsernameNotFoundException("User with username: " + email + " not found");
        }
        return userRepository.findByEmail(email).get();
    }

    @Override
    public UserDetailsService getUserDetailsService() {
        return this::getUserByEmail;
    }

    @Override
    public void enrollUser(User user, Course course) {
        var userOptional = userRepository.findByEmail(user.getEmail());
        if (userOptional.isEmpty()) {
            log.warn("User with email {} does not exist, impossible to enroll to the course: {}", user.getEmail(), course.getCourseName());
            throw new ObjectNotExistException("Нет пользователя с email: " + user.getEmail() + ", невозможно зачислить на курс");
        }
        var userEntity = userOptional.get();
        userEntity.getCourseList().add(course);
        userRepository.save(userEntity);
    }

    @Override
    public void enrollUser(User user, Long courseId) {
        var course = courseService.find(courseId);
        enrollUser(user, course);
    }
}
