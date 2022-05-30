package co.thecodest.conversationbuilder;

import co.thecodest.conversationbuilder.user.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtils {

    public static List<UserDTO> createUsers(int numberOfUsers) {
        final String teamId = "TEAMIDXX001";
        return IntStream.range(0, numberOfUsers)
                .mapToObj(i -> new UserDTO("USERIDXX" + i, teamId, "USER" + i))
                .collect(Collectors.toList());
    }
}
