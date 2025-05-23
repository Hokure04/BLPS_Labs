package labs.blps.kafkalistenerservice.service;

import labs.blps.kafkalistenerservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {
    @Override
    public void register(User user) {
        if(user == null) {
            log.warn("attempt to send null user");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
//        header.add("Authorization", "*****************");
//        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        header.add("Accept", "application/json");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("username", user.getUsername());
        body.add("email", user.getEmail());
        body.add("password", user.getPassword());

        HttpEntity<MultiValueMap<String, String>> requeteHttp = new HttpEntity<MultiValueMap<String, String>>(body, header);

        ResponseEntity<SpiResponse> response = restTemplate.postForEntity("localhost/realms/master/blps-registration/register", requeteHttp, SpiResponse.class);
        log.info("got result: {}", response);
    }
}
