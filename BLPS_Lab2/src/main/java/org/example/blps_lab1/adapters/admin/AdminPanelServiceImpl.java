package org.example.blps_lab1.adapters.admin;

import java.util.Objects;

import org.bouncycastle.eac.EACException;
import org.example.blps_lab1.core.domain.auth.Role;
import org.example.blps_lab1.core.ports.auth.UserService;

import org.example.blps_lab1.core.ports.admin.AdminPanelService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Slf4j
public class AdminPanelServiceImpl implements AdminPanelService {

    private final UserService userService;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public AdminPanelServiceImpl(UserService userService, PlatformTransactionManager transactionManager) {
        this.userService = userService;
        this.transactionManager = transactionManager;
    }

    public void updateRole(String userEmail, String role) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("updateRole");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(definition);
        try{
            if (Objects.isNull(role) || Objects.isNull(userEmail)) {
                throw new IllegalArgumentException("Поля email и role обязательны");
            }
            Role roleToSet;
            try {
                roleToSet = Role.valueOf(role);
            } catch (IllegalArgumentException e) {
                log.error("Invalid argument, got: {}", role, e);
                throw new IllegalArgumentException("Неверно указана роль");
            }

            var user = userService.getUserByEmail(userEmail);
            if (Objects.nonNull(user)) {
                user.setRole(roleToSet);
                userService.updateUser(user);
            }
            transactionManager.commit(status);
        }catch (Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }
}
