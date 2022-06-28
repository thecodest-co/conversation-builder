package co.thecodest.conversationbuilder.external.user.client;

import co.thecodest.conversationbuilder.external.exception.RemoteCallException;
import co.thecodest.conversationbuilder.external.user.dto.UserDTO;
import co.thecodest.conversationbuilder.external.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
            final UserResponseDTO responseDTO = restTemplate.getForObject(userServiceUrl, UserResponseDTO.class);

            if (responseDTO == null || responseDTO.getUsers() == null || responseDTO.getUsers().size() == 0) {
                throw new RemoteCallException("User Rest Api returned null or empty response");
            }

            log.info("User Rest Api call success.");
            return responseDTO.getUsers();
        } catch (RestClientException e) {
            throw new RemoteCallException("User Rest Api call failure", e);
        }
    }

}
