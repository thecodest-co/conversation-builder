package co.thecodest.conversationbuilder.external.message.client;

import co.thecodest.conversationbuilder.external.message.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageClient {

    @Value("${external.service.slack.bot.messages.url}")
    private String messagesServiceUrl;

    private final RestTemplate restTemplate;

    public void sendMessage(final MessageDTO messageDTO) {
        try {
            final String response = restTemplate.postForObject(messagesServiceUrl, messageDTO, String.class);
            log.info("Message Rest Api call success: " + response);
        } catch (RestClientException e) {
            log.error("Message Rest Api call failure. Reason: \n" + e);
        }
    }

}
