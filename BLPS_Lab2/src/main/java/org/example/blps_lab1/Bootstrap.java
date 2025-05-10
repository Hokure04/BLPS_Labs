package org.example.blps_lab1;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.adapters.admin.AdminPanelServiceImpl;
import org.example.blps_lab1.adapters.auth.dto.RegistrationRequestDto;
import org.example.blps_lab1.core.domain.auth.Role;
import org.example.blps_lab1.core.domain.auth.User;
import org.example.blps_lab1.core.domain.auth.UserXml;
import org.example.blps_lab1.core.domain.course.Topic;
import org.example.blps_lab1.core.domain.course.nw.NewCourse;
import org.example.blps_lab1.core.ports.auth.AuthService;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.example.blps_lab1.core.ports.db.UserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class Bootstrap implements ApplicationRunner {

    private final UserDatabase userDatabase;
    private final AuthService authService;
    private final AdminPanelServiceImpl adminPanelService;
    private final NewCourseService courseService;
    private final UserService userService;

    @Value("${app.admin.password}")
    private String adminPass;
    @Value("${app.admin.username}")
    private String adminLogin;

    @Autowired
    public Bootstrap(UserDatabase userDatabase, AuthService authService, AdminPanelServiceImpl adminPanelService, NewCourseService courseService, UserService userService) {
        this.userDatabase = userDatabase;
        this.authService = authService;
        this.adminPanelService = adminPanelService;
        this.courseService = courseService;
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    public void run(ApplicationArguments args) {
//        var regDto = RegistrationRequestDto.builder()
//                .email(adminLogin)
//                .firstName(adminLogin)
//                .lastName(adminLogin)
//                .password(adminPass)
//                .phoneNumber("+7321321321")
//                .companyName(adminLogin)
//                .build();
//        authService.signUp(regDto);


        List<NewCourse> courses = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            var course = NewCourse.builder()
                    .name(UUID.randomUUID().toString())
                    .price(BigDecimal.valueOf(new Random().nextDouble()))
                    .description(UUID.randomUUID().toString())
                    .topic(Topic.ANALYTICS)
                    .build();

            courses.add(course);
        }
        courseService.addAll(courses);
        log.info("was saved: {}", courses.size());

    }
}
