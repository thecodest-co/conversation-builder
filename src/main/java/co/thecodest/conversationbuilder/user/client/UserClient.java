package co.thecodest.conversationbuilder.user.client;

import co.thecodest.conversationbuilder.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Value("${external.api.users.url}")
    private String usersUrl;

    private final RestTemplate restTemplate;

    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        try {
            UserDTO[] remoteUsers = restTemplate.getForObject(usersUrl, UserDTO[].class);
            if (remoteUsers != null) {
                users.addAll(Arrays.asList(remoteUsers));
            }
        } catch (RestClientException e) {
            log.error("User Rest Api call failure. Reason: \n" + e);
        }
        return users;
    }

}
