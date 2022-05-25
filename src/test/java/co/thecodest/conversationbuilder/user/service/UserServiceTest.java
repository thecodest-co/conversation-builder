package co.thecodest.conversationbuilder.user.service;

import co.thecodest.conversationbuilder.user.client.UserClient;
import co.thecodest.conversationbuilder.user.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserService userService;

    private static final int NUMBER_OF_USERS = 10;

    static List<UserDTO> createUsersUpToLimit(int limit) {
        final String teamId = "TEAMIDXX001";
        return IntStream.range(0, limit)
                .mapToObj(i -> new UserDTO("USERIDXX" + i, teamId, "USER" + i))
                .collect(Collectors.toList());
    }

    @Test
    void userServiceSuccessfullyReturnsUsersWhenLimitLowerThanNumberOfUsers() {
        int limit = 8;
        when(userClient.getAllUsers()).thenReturn(createUsersUpToLimit(NUMBER_OF_USERS));

        List<UserDTO> randomUsers = userService.getRandomUsersUpToLimit(limit);

        assertThat(randomUsers.size()).isEqualTo(limit);
    }

    @Test
    void userServiceSuccessfullyReturnsUsersWhenLimitEqualToNumberOfUsers() {
        int limit = NUMBER_OF_USERS;
        when(userClient.getAllUsers()).thenReturn(createUsersUpToLimit(NUMBER_OF_USERS));

        List<UserDTO> randomUsers = userService.getRandomUsersUpToLimit(limit);

        assertThat(randomUsers.size()).isEqualTo(limit);
    }

    @Test
    void userServiceSuccessfullyReturnsUsersWhenLimitHigherThanNumberOfUsers() {
        int limit = 12;
        when(userClient.getAllUsers()).thenReturn(createUsersUpToLimit(NUMBER_OF_USERS));

        List<UserDTO> randomUsers = userService.getRandomUsersUpToLimit(limit);

        assertThat(randomUsers.size()).isEqualTo(NUMBER_OF_USERS);
    }

}