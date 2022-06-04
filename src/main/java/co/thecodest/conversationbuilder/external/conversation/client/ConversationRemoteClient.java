package co.thecodest.conversationbuilder.external.conversation.client;

import co.thecodest.conversationbuilder.external.conversation.dto.ConversationRequestDTO;
import co.thecodest.conversationbuilder.external.conversation.dto.ConversationResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConversationRemoteClient {

    @Value("${external.service.slack.bot.conversations.url}")
    private String conversationServiceUrl;

    private final RestTemplate restTemplate;

    public String createConversation(ConversationRequestDTO conversationRequestDTO) {
        try {
            final ConversationResponseDTO response = restTemplate
                    .postForObject(conversationServiceUrl, conversationRequestDTO, ConversationResponseDTO.class);
            log.info("Conversation Rest Api call success: " + response);
            if (response != null && response.getId() != null) {
                return response.getId();
            }
        } catch (RestClientException e) {
            log.error("Conversation Rest Api call failure. Reason: \n" + e);
        }
        return "";
    }
}
