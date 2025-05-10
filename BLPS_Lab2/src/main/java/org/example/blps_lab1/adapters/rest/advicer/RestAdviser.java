package org.example.blps_lab1.adapters.rest.advicer;

import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.core.exception.auth.AuthorizeException;
import org.example.blps_lab1.core.exception.auth.ApplicationStatusAlreadySetException;
import org.example.blps_lab1.core.exception.common.FieldNotSpecifiedException;
import org.example.blps_lab1.core.exception.common.ObjectAlreadyExistException;
import org.example.blps_lab1.core.exception.common.ObjectNotExistException;
import org.example.blps_lab1.core.exception.common.ObjectNotFoundException;
import org.example.blps_lab1.core.exception.course.InvalidFieldException;
import org.example.blps_lab1.core.exception.course.NotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@Slf4j
public class RestAdviser {
    @ExceptionHandler({FieldNotSpecifiedException.class, IllegalArgumentException.class,
        MailAuthenticationException.class, MailSendException.class, ApplicationStatusAlreadySetException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionWrapper handleFieldNotSpecifiedException(RuntimeException e) {
        return new ExceptionWrapper(e);
    }

    @ExceptionHandler(ObjectAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionWrapper handleObjectAlreadyExistException(ObjectAlreadyExistException e) {
        return new ExceptionWrapper(e);
    }

    @ExceptionHandler(ObjectNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionWrapper handleObjectNotExistException(ObjectNotExistException e) {
        return new ExceptionWrapper(e);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionWrapper handleObjectNotFoundException(ObjectNotFoundException e) {
        return new ExceptionWrapper(e);
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler({AuthorizeException.class})
    public ExceptionWrapper AuthorizeException(RuntimeException e){
        return new ExceptionWrapper(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionWrapper handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return new ExceptionWrapper(new Exception("Произошла внутренняя ошибка сервера"));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionWrapper UsernameNotFoundException(UsernameNotFoundException e){
        return new ExceptionWrapper(e);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionWrapper handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        return new ExceptionWrapper(new Exception("У вас недостаточно прав"));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionWrapper noHandlerFoundException(Exception ex) {
        return new ExceptionWrapper(new Exception("страница не найдена"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionWrapper impossibleToUnmarhal(Exception ex) {
        return new ExceptionWrapper(new Exception("Были получены невалидные данные"));
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ExceptionWrapper methodNotAllowed(Exception ex) {
        return new ExceptionWrapper(new Exception("Метод недоступен по этой ссылке"));
    }

    @ExceptionHandler({NotExistException.class, InvalidFieldException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionWrapper courseNotExistException(Exception ex) {
        return new ExceptionWrapper(ex);
    }
}

