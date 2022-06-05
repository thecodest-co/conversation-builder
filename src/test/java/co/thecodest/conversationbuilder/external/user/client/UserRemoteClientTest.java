package co.thecodest.conversationbuilder.external.user.client;

import co.thecodest.conversationbuilder.external.exception.RemoteCallException;
import co.thecodest.conversationbuilder.external.user.dto.UserDTO;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static co.thecodest.conversationbuilder.TestUtil.createUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(UserRemoteClient.class)
@AutoConfigureWebClient(registerRestTemplate = true)
class UserRemoteClientTest {

    public static final String USERS_URL = "/users-test";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRemoteClient userRemoteClient;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Test
    void userClientSuccessfullyReturnsUsers() throws Exception {
        final List<UserDTO> users = createUsers(3);

        final String usersJson = objectMapper.writeValueAsString(users);

        this.mockRestServiceServer
                .expect(requestTo(USERS_URL))
                .andRespond(withSuccess(usersJson, MediaType.APPLICATION_JSON));

        final List<UserDTO> actualUsers = userRemoteClient.getAllUsers();

        assertThat(actualUsers.size()).isEqualTo(users.size());

        users.forEach(user -> {
            UserDTO actualUser = actualUsers.stream().filter(au -> au.getId().equals(user.getId())).findAny().get();
            assertThat(actualUser.getName()).isEqualTo(user.getName());
            assertThat(actualUser.getTeamId()).isEqualTo(user.getTeamId());
        });
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForUserServiceSuccessfullyReturnsEmptyList")
    void userServiceServiceThrowsException(ResponseCreator remoteCallResponseCreator) {
        this.mockRestServiceServer
                .expect(requestTo(USERS_URL))
                .andRespond(remoteCallResponseCreator);

        assertThrows(RemoteCallException.class, userRemoteClient::getAllUsers);
    }

    private static Stream<Arguments> provideArgumentsForUserServiceSuccessfullyReturnsEmptyList()
            throws JsonProcessingException {
        final String nullUsersList = objectMapper.writeValueAsString(null);
        final String emptyUsersList = objectMapper.writeValueAsString(Collections.emptyList());

        return Stream.of(
                Arguments.of(withSuccess(nullUsersList, MediaType.APPLICATION_JSON)),
                Arguments.of(withSuccess(emptyUsersList, MediaType.APPLICATION_JSON)),
                Arguments.of(withStatus(HttpStatus.BAD_REQUEST)),
                Arguments.of(withStatus(HttpStatus.INTERNAL_SERVER_ERROR))
        );
    }
}
