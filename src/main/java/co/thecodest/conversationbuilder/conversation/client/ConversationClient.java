package co.thecodest.conversationbuilder.conversation.client;

import co.thecodest.conversationbuilder.conversation.dto.ConversationRequestDTO;
import co.thecodest.conversationbuilder.conversation.dto.ConversationResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConversationClient {

    private static final String EMPTY_STRING = "";

    @Value("${external.api.conversations.url}")
    private String conversationsUrl;

    private final RestTemplate restTemplate;

    public String createConversation(ConversationRequestDTO conversationRequestDTO) {
        try {
            final ConversationResponseDTO response = restTemplate
                    .postForObject(conversationsUrl, conversationRequestDTO, ConversationResponseDTO.class);
            log.info("Conversation Rest Api call success: " + response);
            if (response != null && response.getId() != null) {
                return response.getId();
            }
        } catch (RestClientException e) {
            log.error("Conversation Rest Api call failure. Reason: \n" + e);
        }
        return EMPTY_STRING;
    }
}
