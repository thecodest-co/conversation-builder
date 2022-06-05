package co.thecodest.conversationbuilder.external.user.service;

import co.thecodest.conversationbuilder.external.exception.CannotGetUsersException;
import co.thecodest.conversationbuilder.external.exception.RemoteCallException;
import co.thecodest.conversationbuilder.external.user.client.UserRemoteClient;
import co.thecodest.conversationbuilder.external.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRemoteClient userRemoteClient;

    public List<UserDTO> getRandomUsersUpToLimit(long limit) throws CannotGetUsersException {
        try {
            final List<UserDTO> users = userRemoteClient.getAllUsers();
            Collections.shuffle(users);
            return users.stream()
                    .limit(limit)
                    .collect(Collectors.toUnmodifiableList());
        } catch (RemoteCallException e) {
            throw new CannotGetUsersException(e);
        }
    }
}
