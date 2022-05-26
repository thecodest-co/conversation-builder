package co.thecodest.conversationbuilder.meme.client;

import co.thecodest.conversationbuilder.meme.dto.MemeResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(MemeClient.class)
@ExtendWith(MockitoExtension.class)
class MemeClientTest {

    public static final String MEMES_SERVICE_URL = "";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MemeClient memeClient;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @ParameterizedTest
    @MethodSource("provideArgumentsForMemeServiceSuccessfullyReturnsEmptyUrl")
    void memeServiceSuccessfullyReturnsEmptyUrl(ResponseCreator remoteCallResponseCreator) {
        this.mockRestServiceServer
                .expect(requestTo(MEMES_SERVICE_URL))
                .andRespond(remoteCallResponseCreator);

        String actualMemeURL = memeClient.getRandomMemeURL();
        assertThat(actualMemeURL).isEmpty();
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