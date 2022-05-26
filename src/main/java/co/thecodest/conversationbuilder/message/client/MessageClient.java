package co.thecodest.conversationbuilder.message.client;

import co.thecodest.conversationbuilder.message.dto.MessageDTO;
import co.thecodest.conversationbuilder.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageClient {

    @Value("${external.api.messages.url}")
    private String messagesUrl;

    private final RestTemplate restTemplate;

    public void sendMessage(final MessageDTO messageDTO) {
        try {
            final String response = restTemplate.postForObject(messagesUrl, messageDTO, String.class);
            log.info("Message Rest Api call success: " + response);
        } catch (RestClientException e) {
            log.error("Message Rest Api call failure: Reason: \n" + e);
        }
    }

}
