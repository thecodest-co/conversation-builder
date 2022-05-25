package co.thecodest.conversationbuilder.user.client;

import co.thecodest.conversationbuilder.user.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(UserClient.class)
@AutoConfigureWebClient(registerRestTemplate = true)
@TestPropertySource(properties = {"external.api.users.url=/slack/users"})
class UserClientTest {

    @Autowired
    private UserClient userClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Test
    void userClientSuccessfullyReturnsUserDuke() throws Exception {

        final String teamId = "TEAMIDXX001";
        final UserDTO user_1 = new UserDTO("USERIDXX001", teamId, "USER001");
        final UserDTO user_2 = new UserDTO("USERIDXX002", teamId, "USER002");
        final UserDTO user_3 = new UserDTO("USERIDXX003", teamId, "USER003");

        final List<UserDTO> users = new ArrayList<>();
        users.add(user_1);
        users.add(user_2);
        users.add(user_3);

        String usersJson = this.objectMapper
                .writeValueAsString(users);

        this.mockRestServiceServer
                .expect(requestTo("/slack/users"))
                .andRespond(withSuccess(usersJson, MediaType.APPLICATION_JSON));

        final List<UserDTO> actualUsers = userClient.getAllUsers();

        assertThat(actualUsers.size()).isEqualTo(users.size());

        users.forEach(user->{
            UserDTO actualUser = actualUsers.stream().filter(au -> au.getId().equals(user.getId())).findAny().get();
            assertThat(actualUser.getName()).isEqualTo(user.getName());
            assertThat(actualUser.getTeamId()).isEqualTo(user.getTeamId());
        });

    }

}