package co.thecodest.conversationbuilder.external.user.service;

import co.thecodest.conversationbuilder.external.user.client.UserRemoteClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final int GROUP_SIZE = 3;
    @Mock
    private UserRemoteClient userRemoteClient;
    @InjectMocks
    private UserService userService;

    private static Stream<Arguments> provideArgumentsForUserServiceSuccessfullyReturnsUsers() {
        return Stream.of(
                Arguments.of(9, 3),
                Arguments.of(10, 3),
                Arguments.of(11, 6),
                Arguments.of(18, 6),
                Arguments.of(25, 6),
                Arguments.of(26, 9),
                Arguments.of(32, 9),
                Arguments.of(40, 9),
                Arguments.of(41, 12),
                Arguments.of(46, 12),
                Arguments.of(55, 12),
                Arguments.of(56, 15),
                Arguments.of(70, 15),
                Arguments.of(71, 18)
        );
    }

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(userService, "groupSize", GROUP_SIZE);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForUserServiceSuccessfullyReturnsUsers")
    void userServiceSuccessfullyReturnsUsers(int numberOfUsers, int expectedNumberRandomOfUsers) {
//        when(userRemoteClient.getAllUsers()).thenReturn(createUsers(numberOfUsers));
//
//        List<UserDTO> randomUsers = userService.getRandomUsers();
//        assertThat(randomUsers.size()).isEqualTo(expectedNumberRandomOfUsers);
        assertThat(false).isTrue();
    }

}
