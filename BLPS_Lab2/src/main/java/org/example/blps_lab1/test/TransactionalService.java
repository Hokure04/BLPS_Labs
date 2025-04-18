package org.example.blps_lab1.test;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.InitialContext;
import jakarta.transaction.UserTransaction;

@RestController
//NOTICE GENERATED BY CHAT GPT
// DO NOT USE IT
// CAUSE IT DOESN'T WORK :=>
public class TransactionalService {

    @GetMapping("/transaction/manager")
    public ResponseEntity<String> getTransactionManagerInfo() {
        try {
            // Ищем транзакционный менеджер через JNDI
            Object tmObject = new InitialContext().lookup("java:jboss/TransactionManager");
            if (tmObject == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Транзакционный менеджер не найден!");
            }

            // Выводим класс и дополнительную информацию
            String info = "Найден экземпляр транзакционного менеджера: "
                    + tmObject.getClass().getName();
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка: " + e.getMessage());
        }
    }
}
