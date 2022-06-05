package co.thecodest.conversationbuilder.external.meme.client;

import co.thecodest.conversationbuilder.external.exception.RemoteCallException;
import co.thecodest.conversationbuilder.external.meme.dto.MemeResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestClientTest(MemeRemoteClient.class)
@ExtendWith(MockitoExtension.class)
class MemeRemoteClientTest {

    public static final String MEMES_SERVICE_URL = "https://some-random-api-test.ml/meme";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MemeRemoteClient memeRemoteClient;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Test
    void memeServiceSuccessfullyReturnsUrl() throws Exception {
        final String memeUrl = "https://test-url.pl/meme.jpg";
        final MemeResponseDTO response = new MemeResponseDTO(1L, memeUrl, "", "");

        final String responseJson = objectMapper.writeValueAsString(response);

        this.mockRestServiceServer
                .expect(requestTo(MEMES_SERVICE_URL))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        String actualMemeURL = memeRemoteClient.getRandomMemeURL();
        assertThat(actualMemeURL).isEqualTo(memeUrl);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForMemeServiceSuccessfullyReturnsEmptyUrl")
    void memeServiceThrowsException(ResponseCreator remoteCallResponseCreator) {
        this.mockRestServiceServer
                .expect(requestTo(MEMES_SERVICE_URL))
                .andRespond(remoteCallResponseCreator);

        assertThrows(RemoteCallException.class, memeRemoteClient::getRandomMemeURL);
    }

    private static Stream<Arguments> provideArgumentsForMemeServiceSuccessfullyReturnsEmptyUrl()
            throws JsonProcessingException {
        final String nullResponse = objectMapper.writeValueAsString(null);
        final String nullUrlInResponse = objectMapper.writeValueAsString(
                new MemeResponseDTO(1L, null, null, null));

        return Stream.of(
                Arguments.of(withSuccess(nullResponse, MediaType.APPLICATION_JSON)),
                Arguments.of(withSuccess(nullUrlInResponse, MediaType.APPLICATION_JSON)),
                Arguments.of(withStatus(HttpStatus.BAD_REQUEST)),
                Arguments.of(withStatus(HttpStatus.INTERNAL_SERVER_ERROR))
        );
    }
}
