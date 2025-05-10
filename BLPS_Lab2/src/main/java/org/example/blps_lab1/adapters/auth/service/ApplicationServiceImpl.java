package org.example.blps_lab1.adapters.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.adapters.db.auth.ApplicationRepository;
import org.example.blps_lab1.core.domain.auth.Application;
import org.example.blps_lab1.core.domain.auth.ApplicationStatus;
import org.example.blps_lab1.core.domain.auth.UserXml;
import org.example.blps_lab1.core.exception.auth.ApplicationStatusAlreadySetException;
import org.example.blps_lab1.core.exception.common.ObjectNotExistException;
import org.example.blps_lab1.core.ports.auth.ApplicationService;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.example.blps_lab1.core.ports.course.nw.NewCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository repository;
    private final UserService userService;
    private final NewCourseService courseService;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository repository, UserService userService, NewCourseService courseService, PlatformTransactionManager transactionManager) {
        this.repository = repository;
        this.userService = userService;
        this.courseService = courseService;
        this.transactionManager = transactionManager;
    }

    @Override
    public Application add(UUID courseUUID) {
        var userEntity = getCurrentUser();
        return add(courseUUID, userEntity);
    }

    @Override
    public Application add(UUID courseUUID, UserXml user) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("add");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            var courseEntity = courseService.find(courseUUID);
            var app = Application.builder()
                    .newCourse(courseEntity)
                    .userEmail(user.getUsername())
                    .status(ApplicationStatus.PENDING)
                    .build();
            log.debug("attempt to create application: {}", app);
            transactionManager.commit(status);
            return repository.save(app);
        }catch (Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public Application updateStatus(Long id, ApplicationStatus applicationStatus) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("updateStatus");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            Optional<Application> oldEntityOptional = repository.findById(id);
            if (oldEntityOptional.isEmpty()) {
                log.warn("Application with id: {} did not exist", id);
                throw new ObjectNotExistException("Заявки с id: " + id + "  не существует");
            }
            var entity = oldEntityOptional.get();
            if (entity.getStatus() != ApplicationStatus.PENDING) {
                throw new ApplicationStatusAlreadySetException("Нельзя изменить статус уже сформированной заявки");
            }
            entity.setStatus(applicationStatus);
            transactionManager.commit(status);
            return repository.save(entity);
        }catch (Exception e){
            transactionManager.rollback(status);
            throw e;
        }

    }

    @Override
    public List<Application> find(UUID courseUUID) {
        return repository.findByNewCourse_Uuid(courseUUID);
    }

    @Override
    public void remove(List<Application> applicationList) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("remove");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            repository.deleteAll(applicationList);
            transactionManager.commit(status);
        }catch (Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }

    private UserXml getCurrentUser() {
//        copypaste cause depend cycle
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userService.getUserByEmail(username);
        } else {
            throw new IllegalStateException("Current user is not authenticated");
        }
    }

}
