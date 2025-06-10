package org.example.blps_lab1.adapters.bootstrap;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.adapters.auth.dto.RegistrationRequestDto;
import org.example.blps_lab1.adapters.course.dto.nw.NewCourseDto;
import org.example.blps_lab1.adapters.course.dto.nw.NewExerciseDto;
import org.example.blps_lab1.adapters.course.dto.nw.NewModuleDto;
import org.example.blps_lab1.core.domain.auth.Role;
import org.example.blps_lab1.core.domain.course.Topic;
import org.example.blps_lab1.core.domain.course.nw.NewCourse;
import org.example.blps_lab1.core.ports.auth.ApplicationService;
import org.example.blps_lab1.core.ports.auth.AuthService;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.example.blps_lab1.core.ports.course.nw.NewExerciseService;
import org.example.blps_lab1.core.ports.course.nw.NewModuleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class Bootstrap implements CommandLineRunner {

    private final AuthService authService;
    private final NewCourseService newCourseService;
    private final UserService userService;
    private final ApplicationService applicationService;
    private final EntityManager em;
    private final NewModuleService newModuleService;
    private final NewExerciseService newExerciseService;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        NewCourse newCourse = null;
        for (int i = 0; i < 1; i++) {
            NewExerciseDto exerciseDto = NewExerciseDto
                    .builder()
                    .points(21)
                    .name("demo" + i)
                    .description("demo" + i)
                    .answer("ans")
                    .build();
            newExerciseService.createNewExercise(exerciseDto);
            em.flush();

            NewModuleDto newModuleDto = NewModuleDto
                    .builder()
                    .name("demo" + i)
                    .description("demo" + i)
                    .exercises(List.of(exerciseDto))
                    .build();
            newModuleService.createModule(newModuleDto);
            em.flush();

            NewCourseDto nw = NewCourseDto.builder()
                    .name("demo" + i)
                    .description("ааа описание" + i)
                    .price(new BigDecimal(99))
                    .topic(Topic.PROGRAMMING)
                    .newModuleList(List.of(newModuleDto))
                    .build();
            newCourse = newCourseService.createCourse(nw);
            em.flush();
        }

        RegistrationRequestDto registrationRequestDto = RegistrationRequestDto.builder()
                .firstName("demo")
                .lastName("demo")
                .phoneNumber("+79999999999")
                .userID("demo") //придолбили к дефолтному юзеру camunda
                .email("demo@demo.demo")
                .password("demo")
                .build();

        authService.signUp(registrationRequestDto, newCourse.getUuid());
        em.flush();

        log.info("finish registration");

        var user = userService.getUserByEmail(registrationRequestDto.getEmail());
        user.setRole(Role.ROLE_ADMIN);
        userService.add(user);
        log.info("role admin is set for user {}", user);
    }
}
