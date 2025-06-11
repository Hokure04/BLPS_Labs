package org.example.blps_lab1.adapters.miniservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.adapters.miniservice.model.MiniUser;
import org.example.blps_lab1.adapters.miniservice.model.UserFailure;
import org.example.blps_lab1.adapters.miniservice.repository.UserFailureRepository;
import org.example.blps_lab1.adapters.miniservice.service.RegisterService;
import org.example.blps_lab1.adapters.miniservice.service.UserFailureService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserFailureServiceImpl implements UserFailureService {

    private final UserFailureRepository userFailureRepository;
    private final RegisterService registerService;

    @Override
    public void saveFailedUser(UserFailure userFailure) {
        if (userFailure == null || userFailure.getUsername().isEmpty() || userFailure.getEmail().isEmpty() || userFailure.getPassword().isEmpty()) {
            log.error("attempt to save invalid user failure");
            return;
        }

        userFailureRepository.save(userFailure);
    }

    @Override
    @Transactional
//    @Scheduled(fixedRate = 5000)
    public void recoverAll() {
        List<UserFailure> userFailureList = userFailureRepository.findAllByIsFailed(Boolean.TRUE);
        log.info("starting to update users, size to update {}", userFailureList.size());

        List<UserFailure> successfullyUpdated = new ArrayList<>();
        for (var u : userFailureList) {
            var userDto = MiniUser
                    .builder()
                    .username(u.getUsername())
                    .email(u.getEmail())
                    .password(u.getEmail())
                    .build();
            if (registerService.register(userDto)) {
                log.info("successfully recover user: {}", userDto);
                u.setIsFailed(Boolean.FALSE);
                successfullyUpdated.add(u);
                continue;
            }
            log.error("user: {} wasn't updated", userDto);
        }

        userFailureRepository.saveAll(successfullyUpdated);
    }
}
