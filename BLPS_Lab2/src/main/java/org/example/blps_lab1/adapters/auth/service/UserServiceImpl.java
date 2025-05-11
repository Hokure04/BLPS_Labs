package org.example.blps_lab1.adapters.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.core.domain.auth.UserXml;
import org.example.blps_lab1.core.domain.course.nw.NewCourse;
import org.example.blps_lab1.core.exception.common.ObjectNotExistException;
import org.example.blps_lab1.core.ports.auth.UserService;
import org.example.blps_lab1.core.ports.db.UserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDatabase userRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public UserServiceImpl(UserDatabase userRepository, PlatformTransactionManager transactionManager) {
        this.userRepository = userRepository;
        this.transactionManager = transactionManager;
    }


    @Override
    public UserXml add(final UserXml user) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("addUserTransaction");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            user.setPassword(user.getPassword());
            UserXml savedUser = userRepository.save(user);
            log.info("{} registered successfully", user.getUsername());
            transactionManager.commit(status);
            return savedUser;
        }catch (Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }


    @Override
    public UserXml updateUser(final UserXml user) {
        UserXml newUser = userRepository.save(user);
        log.info("{} updated successfully", user.getUsername());
        return newUser;
    }


    @Override
    public boolean isExist(final String email) {
        Optional<UserXml> potentialUser = userRepository.findByEmail(email);
        if (potentialUser.isPresent()) {
            log.info("User with username: {} exist", email);
            return true;
        }
        log.info("User with username: {} not exist", email);
        return false;
    }

    @Override
    public UserXml getUserByEmail(final String email) {
        return userRepository.findByEmail(email.trim()).orElseThrow(() -> new UsernameNotFoundException("User with username: " + email + " not found"));
    }

    @Override
    public UserDetailsService getUserDetailsService() {
        return this::getUserByEmail;
    }

    @Override
    public void enrollUser(UserXml user, NewCourse course) {
       DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
       definition.setName("enrollUserInUserServiceImpl");
       definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
       TransactionStatus status = transactionManager.getTransaction(definition);
       try{
           var userOptional = userRepository.findByEmail(user.getUsername());
           var userEntity = userOptional.orElseThrow(() -> new ObjectNotExistException("Нет пользователя с email: " + user.getUsername() + ", невозможно зачислить на курс"));
           userRepository.save(userEntity);
           transactionManager.commit(status);
       }catch (Exception e){
           transactionManager.rollback(status);
           throw e;
       }
    }

}
