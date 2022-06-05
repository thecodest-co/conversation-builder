package co.thecodest.conversationbuilder.external.message.client;

import co.thecodest.conversationbuilder.external.exception.RemoteCallException;
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
public class MessageRemoteClient {

    @Value("${external.service.slack.bot.messages.url}")
    private String messagesServiceUrl;

    private final RestTemplate restTemplate;

    public void sendMessage(final MessageDTO messageDTO) throws RemoteCallException {
        try {
            restTemplate.postForObject(messagesServiceUrl, messageDTO, String.class);
            log.info("Message Rest Api call success");
        } catch (RestClientException e) {
            throw new RemoteCallException("Message Rest Api call failure", e);
        }
    }

}
