package co.thecodest.conversationbuilder.external.user.client;

import co.thecodest.conversationbuilder.external.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserClient {

    @Value("${external.service.slack.bot.users.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        try {
            UserDTO[] remoteUsers = restTemplate.getForObject(userServiceUrl, UserDTO[].class);
            if (remoteUsers != null) {
                users.addAll(Arrays.asList(remoteUsers));
            }
        } catch (RestClientException e) {
            log.error("User Rest Api call failure. Reason: \n" + e);
        }
        return users;
    }

}
