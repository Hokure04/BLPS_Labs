package labs.blps.kafkalistenerservice.service;

import labs.blps.kafkalistenerservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final int MAX_ATTEMPTS = 5;
    private static final long RETRY_DELAY_MS = 2000;

    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public void register(User user) {
        if (user == null) {
            log.warn("attempt to send null user");
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<User> request = new HttpEntity<>(user, headers);

        try {
            ResponseEntity<SpiResponse> response =
                    restTemplate.postForEntity(REGISTER_URL, request, SpiResponse.class);
            log.info("got result: {}", response);
        } catch (HttpClientErrorException e) {
            log.error("error registering user: {} → {}", e.getStatusCode(), e.getResponseBodyAsString());
        }

    }
}
