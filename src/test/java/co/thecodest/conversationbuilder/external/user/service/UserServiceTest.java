package co.thecodest.conversationbuilder.external.user.service;

import co.thecodest.conversationbuilder.external.user.client.UserRemoteClient;
import co.thecodest.conversationbuilder.external.user.dto.UserDTO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static co.thecodest.conversationbuilder.TestUtil.createUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final int NUMBER_OF_USERS = 10;
    @Mock
    private UserRemoteClient userRemoteClient;
    @InjectMocks
    private UserService userService;

    private static Stream<Arguments> provideArgumentsForUserServiceSuccessfullyReturnsUsers() {
        int limitLowerThanNumberOfUsers = NUMBER_OF_USERS - 1;
        int limitHigherThanNumberOfUsers = NUMBER_OF_USERS + 5;
        return Stream.of(
                Arguments.of(limitLowerThanNumberOfUsers),
                Arguments.of(limitHigherThanNumberOfUsers),
                Arguments.of(NUMBER_OF_USERS)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForUserServiceSuccessfullyReturnsUsers")
    void userServiceSuccessfullyReturnsUsers(int limit) {
        when(userRemoteClient.getAllUsers()).thenReturn(createUsers(NUMBER_OF_USERS));

        List<UserDTO> randomUsers = userService.getRandomUsersUpToLimit(limit);

        int expectedNumberOfUsers = Math.min(limit, NUMBER_OF_USERS);

        assertThat(randomUsers.size()).isEqualTo(expectedNumberOfUsers);
    }

}
