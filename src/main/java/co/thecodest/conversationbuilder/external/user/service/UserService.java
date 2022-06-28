package co.thecodest.conversationbuilder.external.user.service;

import co.thecodest.conversationbuilder.external.exception.CannotGetUsersException;
import co.thecodest.conversationbuilder.external.exception.RemoteCallException;
import co.thecodest.conversationbuilder.external.user.client.UserRemoteClient;
import co.thecodest.conversationbuilder.external.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${conversation.builder.group.size}")
    private int groupSize;

    private final UserRemoteClient userRemoteClient;

    public List<UserDTO> getRandomUsers() throws CannotGetUsersException {
        try {
            final List<UserDTO> users = userRemoteClient.getAllUsers();
            Collections.shuffle(users);
            int limit = getLimitDivisibleByGroupSize(users.size());
            return users.stream()
                    .limit(limit)
                    .collect(Collectors.toUnmodifiableList());
        } catch (RemoteCallException e) {
            throw new CannotGetUsersException(e);
        }
    }

    private int getLimitDivisibleByGroupSize(int size) {
        if (size < groupSize) {
            return size;
        }
        int oneFifthOfSize = (int) Math.ceil(size * 0.2f);
        int mod = oneFifthOfSize % groupSize;
        return oneFifthOfSize + groupSize - mod;
    }
}
