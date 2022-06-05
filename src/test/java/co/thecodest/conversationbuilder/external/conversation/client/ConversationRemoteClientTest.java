package co.thecodest.conversationbuilder.external.conversation.client;

import co.thecodest.conversationbuilder.external.conversation.dto.ConversationRequestDTO;
import co.thecodest.conversationbuilder.external.conversation.dto.ConversationResponseDTO;
import co.thecodest.conversationbuilder.external.exception.RemoteCallException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ConversationRemoteClient.class)
@AutoConfigureWebClient(registerRestTemplate = true)
class ConversationRemoteClientTest {

    public static final String CONVERSATIONS_SERVICE_URL = "/conversations-test";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ConversationRemoteClient conversationRemoteClient;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Test
    void conversationServiceSuccessfullyReturnsConversationId() throws Exception {
        final String conversationId = "C03HRV5KK1P";
        final ConversationResponseDTO response = new ConversationResponseDTO(conversationId, "");

        final String responseJson = objectMapper.writeValueAsString(response);

        this.mockRestServiceServer
                .expect(requestTo(CONVERSATIONS_SERVICE_URL))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        String actualMemeURL = conversationRemoteClient.createConversation(new ConversationRequestDTO());
        assertThat(actualMemeURL).isEqualTo(conversationId);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForConversationsServiceSuccessfullyReturnsEmptyString")
    void conversationServiceSuccessfullyReturnsEmptyString(ResponseCreator remoteCallResponseCreator) {
        this.mockRestServiceServer
                .expect(requestTo(CONVERSATIONS_SERVICE_URL))
                .andRespond(remoteCallResponseCreator);

        assertThrows(RemoteCallException.class,
                () -> conversationRemoteClient.createConversation(new ConversationRequestDTO()));

    }

    private static Stream<Arguments> provideArgumentsForConversationsServiceSuccessfullyReturnsEmptyString()
            throws JsonProcessingException {
        final String nullResponse = objectMapper.writeValueAsString(null);
        final String nullIdInResponse = objectMapper.writeValueAsString(
                new ConversationResponseDTO(null, null));

        return Stream.of(
                Arguments.of(withSuccess(nullResponse, MediaType.APPLICATION_JSON)),
                Arguments.of(withSuccess(nullIdInResponse, MediaType.APPLICATION_JSON)),
                Arguments.of(withStatus(HttpStatus.BAD_REQUEST)),
                Arguments.of(withStatus(HttpStatus.INTERNAL_SERVER_ERROR))
        );
    }

}
