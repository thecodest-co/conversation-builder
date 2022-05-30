package co.thecodest.conversationbuilder.external.user.service;

import co.thecodest.conversationbuilder.external.user.client.UserClient;
import co.thecodest.conversationbuilder.external.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserClient userClient;

    public List<UserDTO> getRandomUsersUpToLimit(long limit) {
        final List<UserDTO> users = userClient.getAllUsers();
        Collections.shuffle(users);
        return users.stream()
                .limit(limit)
                .collect(Collectors.toUnmodifiableList());
    }
}
