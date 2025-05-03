package org.example.blps_lab1.adapters.auth.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.example.blps_lab1.adapters.auth.dto.ApplicationResponseDto;
import org.example.blps_lab1.adapters.auth.dto.JwtAuthenticationResponse;
import org.example.blps_lab1.adapters.auth.dto.LoginRequest;
import org.example.blps_lab1.adapters.auth.dto.RegistrationRequestDto;
import org.example.blps_lab1.core.domain.auth.UserXml;
import org.example.blps_lab1.core.exception.auth.AuthorizeException;
import org.example.blps_lab1.core.domain.auth.Role;
import org.example.blps_lab1.core.exception.course.CourseNotExistException;
import org.example.blps_lab1.core.domain.auth.User;
import org.example.blps_lab1.core.ports.auth.ApplicationService;
import org.example.blps_lab1.core.ports.auth.AuthService;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.example.blps_lab1.core.exception.common.FieldNotSpecifiedException;

import org.example.blps_lab1.core.exception.common.ObjectNotExistException;
import org.example.blps_lab1.core.ports.course.CourseService;
import org.example.blps_lab1.core.ports.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private CourseService courseService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;
    private final ApplicationService applicationService;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public AuthServiceImpl(CourseService courseService, PasswordEncoder passwordEncoder,
                           JwtService jwtService, UserService userService,
                           ApplicationService applicationService,
                           PlatformTransactionManager transactionManager) {
        this.courseService = courseService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userService = userService;
        this.applicationService = applicationService;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    /**
     * Возвращает готового пользователя, собранного из <code>RegistrationRequestDto</code>
     *
     * @param request RegistrationRequestDto
     * @return {@link User}, которого можно сохранять в бд
     * @throws AuthorizeException, если пользователь с таким именем существует
     *          {@link }
     */
    private UserXml getUserOrThrow(RegistrationRequestDto request) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(request.getEmail());
        if (!matcher.matches()){
            log.error("error, email expect domain, got {}", request.getEmail());
            throw new IllegalArgumentException("Email должен включать в себя домен");
        }

        var userBuilder = UserXml.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.CASUAL_STUDENT)
                .password(passwordEncoder.encode(request.getPassword()));
        var user = userBuilder.build();

        if (userService.isExist(user.getUsername())) {
            log.warn("User with username: {} exist", user.getUsername());
            throw new AuthorizeException("Пользователь с именем: " + user.getUsername() +
                    " уже существует");
        }
        return user;
    }

    /**
     * Регистрация пользователя без записи на курс
     *
     * @param request включает в себя поля для регистрации поля
     * @return обертку с JWT токеном внутри
     */
    @Override
    public JwtAuthenticationResponse signUp(RegistrationRequestDto request) {
        return transactionTemplate.execute(status -> {
            var user = getUserOrThrow(request);
            userService.add(user);
            var jwt = jwtService.generateToken(user);
            return new JwtAuthenticationResponse(jwt);
        });
    }

    /**
     * Регистрация пользователя с записью на курс
     *
     * @param request    включает в себя поля для регистрации поля
     * @param courseUUID uuid курса, на который записывается пользователь.
     *                   Если курса не существует, выбрасывает ошибку {@link CourseNotExistException}
     *                   Если uuid не указан, выбрасывает ошибку {@link FieldNotSpecifiedException}
     * @return {@link ApplicationResponseDto}, который включает в себя
     * jwt токен {@link JwtAuthenticationResponse} и информацию о заявке(цену и описание)
     */
    @Override
    public ApplicationResponseDto signUp(RegistrationRequestDto request, Long courseUUID) {
        return transactionTemplate.execute(status -> {
            var resultBuilder = ApplicationResponseDto.builder();
            var user = getUserOrThrow(request);
            if (courseUUID == null) {
                log.warn("course id is not specified, request: {}", request);
                throw new FieldNotSpecifiedException("Не указан id курса");
            }
            try {
                courseService.getCourseByID(courseUUID);
            } catch (ObjectNotExistException e) {
                log.warn("course with uuid: {} not found", courseUUID);
                throw new CourseNotExistException("ошибка при создании заявки: данного курса больше не существует");
            }
            var userEntity = userService.add(user);
            var applicationEntity = applicationService.add(courseUUID, userEntity);
            resultBuilder.applicationID(applicationEntity.getId());

            var jwt = jwtService.generateToken(user);
            resultBuilder.jwt(new JwtAuthenticationResponse(jwt));
            return resultBuilder.build();
        });
    }


    @Override
    public JwtAuthenticationResponse signIn(LoginRequest request) {
        return transactionTemplate.execute(status -> {
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                throw new FieldNotSpecifiedException("Поле email обязательное");
            }
            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                throw new FieldNotSpecifiedException("Поле password обязательное");
            }

            UserXml userEntity;
            try {
                userEntity = userService.getUserByEmail(request.getEmail());
                log.debug("Stored hash: {}", userEntity.getPassword());

                if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
                    throw new AuthorizeException("Пароль указан неверно");
                }
            } catch (UsernameNotFoundException e) {
                throw new AuthorizeException("Пользователя с заданным email не существует");
            }

            var jwt = jwtService.generateToken(userEntity);
            return new JwtAuthenticationResponse(jwt);
        });
    }

    @Override
    public UserXml getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userService.getUserByEmail(username);
        } else {
            throw new AuthorizeException("Текущий пользователь не авторизован");
        }
    }
}
