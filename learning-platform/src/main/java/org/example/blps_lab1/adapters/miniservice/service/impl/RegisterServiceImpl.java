package org.example.blps_lab1.adapters.miniservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.blps_lab1.adapters.miniservice.model.MiniUser;
import org.example.blps_lab1.adapters.miniservice.model.UserFailure;
import org.example.blps_lab1.adapters.miniservice.repository.UserFailureRepository;
import org.example.blps_lab1.adapters.miniservice.service.RegisterService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {

    private static final String REGISTER_URL =
            "http://localhost/realms/master/blps-registration/register";

    private final RestTemplate restTemplate = new RestTemplate();

    private final UserFailureRepository userFailureRepository;

    public RegisterServiceImpl(UserFailureRepository userFailureRepository) {
        this.userFailureRepository = userFailureRepository;
    }


    @Override
    public Boolean register(MiniUser user) {
        if (user == null) {
            log.warn("attempt to send null user");
            return false;
        }

        HttpHeaders headers = new HttpHeaders();
        user.setPassword(user.getEmail());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<MiniUser> request = new HttpEntity<>(user, headers);

        try {
            ResponseEntity<SpiResponse> response =
                    restTemplate.postForEntity(REGISTER_URL, request, SpiResponse.class);
            log.info("got result: {}", response);
            return true;
        } catch (HttpClientErrorException e) {
            log.error("error registering user: {} → {}", e.getStatusCode(), e.getResponseBodyAsString());
            return false;
        } catch (Exception e){
            log.error("server has occured an exception, will attempt later");

            var userFailure = new UserFailure();
            userFailure.setUsername(user.getUsername());
            userFailure.setPassword(user.getEmail());
            userFailure.setEmail(user.getEmail());
            userFailureRepository.save(userFailure);
            return false;
        }
    }
}
