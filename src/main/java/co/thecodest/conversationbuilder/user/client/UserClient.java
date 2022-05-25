package co.thecodest.conversationbuilder.user.client;

import co.thecodest.conversationbuilder.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserClient {

    @Value("${external.api.users.url}")
    private String usersUrl;

    private final RestTemplate restTemplate;

    public List<UserDTO> getAllUsers() {
        UserDTO[] users = restTemplate.getForObject(usersUrl, UserDTO[].class);
        return Arrays.asList(users);
    }
}
