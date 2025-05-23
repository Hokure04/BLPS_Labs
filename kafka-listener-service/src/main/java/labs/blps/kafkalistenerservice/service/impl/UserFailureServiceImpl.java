package labs.blps.kafkalistenerservice.service.impl;

import jakarta.transaction.Transactional;
import labs.blps.kafkalistenerservice.model.User;
import labs.blps.kafkalistenerservice.model.UserFailure;
import labs.blps.kafkalistenerservice.repository.UserFailureRepository;
import labs.blps.kafkalistenerservice.service.RegisterService;
import labs.blps.kafkalistenerservice.service.UserFailureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        if(userFailure == null || userFailure.getUsername().isEmpty() || userFailure.getEmail().isEmpty() || userFailure.getPassword().isEmpty()) {
            log.error("attempt to save invalid user failure");
            return;
        }

        userFailureRepository.save(userFailure);
    }

    @Override
    @Transactional
    public void recoverAll() {
        List<UserFailure> userFailureList = userFailureRepository.findAllByIsFailed(Boolean.TRUE);

        List<UserFailure> successfullyUpdated = new ArrayList<>();
        for(var u : userFailureList) {
            var userDto = User
                    .builder()
                    .username(u.getUsername())
                    .email(u.getEmail())
                    .password(u.getPassword())
                    .build();
            try {
                registerService.register(userDto);

                u.setIsFailed(Boolean.FALSE);
                successfullyUpdated.add(u);
            }catch (Exception e) {
                log.error("fail to recover user {}", userDto);
            }
        }

        userFailureRepository.saveAll(successfullyUpdated);
    }
}
