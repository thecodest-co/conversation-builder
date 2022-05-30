package co.thecodest.conversationbuilder;

import co.thecodest.conversationbuilder.external.user.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtil {

    public static List<UserDTO> createUsers(int numberOfUsers) {
        final String teamId = "TEAMIDXX001";
        final String userIdPrefix = "USERIDXX";
        final String userNamePrefix = "USER";
        return IntStream.range(0, numberOfUsers)
                .mapToObj(i -> new UserDTO(userIdPrefix + i, teamId, userNamePrefix + i))
                .collect(Collectors.toList());
    }
}
