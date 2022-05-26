package co.thecodest.conversationbuilder.user.client;

import co.thecodest.conversationbuilder.user.dto.UserDTO;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(UserClient.class)
@AutoConfigureWebClient(registerRestTemplate = true)
class UserClientTest {

    public static final String USERS_URL = "/slack/users";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserClient userClient;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Test
    void userClientSuccessfullyReturnsUsers() throws Exception {
        final String teamId = "TEAMIDXX001";
        final UserDTO user_1 = new UserDTO("USERIDXX001", teamId, "USER001");
        final UserDTO user_2 = new UserDTO("USERIDXX002", teamId, "USER002");
        final UserDTO user_3 = new UserDTO("USERIDXX003", teamId, "USER003");

        final List<UserDTO> users = new ArrayList<>();
        users.add(user_1);
        users.add(user_2);
        users.add(user_3);

        final String usersJson = objectMapper.writeValueAsString(users);

        this.mockRestServiceServer
                .expect(requestTo(USERS_URL))
                .andRespond(withSuccess(usersJson, MediaType.APPLICATION_JSON));

        final List<UserDTO> actualUsers = userClient.getAllUsers();

        assertThat(actualUsers.size()).isEqualTo(users.size());

        users.forEach(user -> {
            UserDTO actualUser = actualUsers.stream().filter(au -> au.getId().equals(user.getId())).findAny().get();
            assertThat(actualUser.getName()).isEqualTo(user.getName());
            assertThat(actualUser.getTeamId()).isEqualTo(user.getTeamId());
        });
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForUserServiceSuccessfullyReturnsEmptyList")
    void userServiceSuccessfullyReturnsEmptyList(ResponseCreator remoteCallResponseCreator) {
        this.mockRestServiceServer
                .expect(requestTo(USERS_URL))
                .andRespond(remoteCallResponseCreator);

        final List<UserDTO> actualUsers = userClient.getAllUsers();
        assertThat(actualUsers).isEmpty();
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