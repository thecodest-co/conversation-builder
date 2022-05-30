package co.thecodest.conversationbuilder.user.service;

import co.thecodest.conversationbuilder.user.client.UserClient;
import co.thecodest.conversationbuilder.user.dto.UserDTO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static co.thecodest.conversationbuilder.TestUtils.createUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserService userService;

    private static final int NUMBER_OF_USERS = 10;

    @ParameterizedTest
    @MethodSource("provideArgumentsForUserServiceSuccessfullyReturnsUsers")
    void userServiceSuccessfullyReturnsUsers(int limit) {
        when(userClient.getAllUsers()).thenReturn(createUsers(NUMBER_OF_USERS));

        List<UserDTO> randomUsers = userService.getRandomUsersUpToLimit(limit);

        int expectedNumberOfUsers = Math.min(limit, NUMBER_OF_USERS);

        assertThat(randomUsers.size()).isEqualTo(expectedNumberOfUsers);
    }


    private static Stream<Arguments> provideArgumentsForUserServiceSuccessfullyReturnsUsers() {
        int limitLowerThanNumberOfUsers = NUMBER_OF_USERS - 1;
        int limitHigherThanNumberOfUsers = NUMBER_OF_USERS + 5;
        return Stream.of(
                Arguments.of(limitLowerThanNumberOfUsers),
                Arguments.of(limitHigherThanNumberOfUsers),
                Arguments.of(NUMBER_OF_USERS)
        );
    }

}