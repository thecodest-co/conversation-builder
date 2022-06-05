package co.thecodest.conversationbuilder.external.user.client;

import co.thecodest.conversationbuilder.external.exception.RemoteCallException;
import co.thecodest.conversationbuilder.external.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRemoteClient {

    @Value("${external.service.slack.bot.users.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public List<UserDTO> getAllUsers() throws RemoteCallException {
        try {
            final UserDTO[] remoteUsers = restTemplate.getForObject(userServiceUrl, UserDTO[].class);

            if (remoteUsers == null || remoteUsers.length == 0) {
                throw new RemoteCallException("User Rest Api returned null or empty response");
            }

            log.info("User Rest Api call success.");
            return Arrays.asList(remoteUsers);
        } catch (RestClientException e) {
            throw new RemoteCallException("User Rest Api call failure", e);
        }
    }

}
