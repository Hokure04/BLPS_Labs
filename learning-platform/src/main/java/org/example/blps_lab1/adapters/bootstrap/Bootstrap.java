package org.example.blps_lab1.adapters.bootstrap;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.adapters.auth.dto.RegistrationRequestDto;
import org.example.blps_lab1.adapters.course.dto.nw.NewCourseDto;
import org.example.blps_lab1.core.domain.auth.Role;
import org.example.blps_lab1.core.domain.course.Topic;
import org.example.blps_lab1.core.domain.course.nw.NewCourse;
import org.example.blps_lab1.core.ports.auth.ApplicationService;
import org.example.blps_lab1.core.ports.auth.AuthService;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class Bootstrap implements CommandLineRunner {

    private final AuthService authService;
    private final NewCourseService newCourseService;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        RegistrationRequestDto registrationRequestDto = RegistrationRequestDto.builder()
                .firstName("demo")
                .lastName("demo")
                .phoneNumber("+79999999999")
                .userID("demo") //придолбили к дефолтному юзеру camunda
                .email("demo@demo.demo")
                .password("demo")
                .build();

        authService.signUp(registrationRequestDto);

        log.info("finish registration");

        var user = userService.getUserByEmail(registrationRequestDto.getEmail());
        user.setRole(Role.ROLE_ADMIN);
        userService.add(user);
        log.info("role admin is set for user {}", user);


        for (int i = 0; i < 2; i++) {
            NewCourseDto nw = NewCourseDto.builder()
                    .name("demo" + i)
                    .description("ааа описание" + i)
                    .price(new BigDecimal(99))
                    .topic(Topic.PROGRAMMING)
                    .build();
            newCourseService.createCourse(nw);
        }

    }
}
