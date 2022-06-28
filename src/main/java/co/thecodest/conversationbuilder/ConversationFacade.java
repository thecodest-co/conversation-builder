package co.thecodest.conversationbuilder;

import co.thecodest.conversationbuilder.external.conversation.client.ConversationRemoteClient;
import co.thecodest.conversationbuilder.external.conversation.dto.ConversationRequestDTO;
import co.thecodest.conversationbuilder.external.exception.CannotGetUsersException;
import co.thecodest.conversationbuilder.external.exception.RemoteCallException;
import co.thecodest.conversationbuilder.external.meme.client.MemeRemoteClient;
import co.thecodest.conversationbuilder.external.message.client.MessageRemoteClient;
import co.thecodest.conversationbuilder.external.message.dto.MessageDTO;
import co.thecodest.conversationbuilder.external.user.dto.UserDTO;
import co.thecodest.conversationbuilder.external.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static co.thecodest.conversationbuilder.util.ListUtil.batches;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConversationFacade {

    private static final String MEME_URL_PLACEHOLDER = "<meme-url>";
    private final UserService userService;
    private final ConversationRemoteClient conversationRemoteClient;
    private final MemeRemoteClient memeRemoteClient;
    private final MessageRemoteClient messageRemoteClient;
    @Value("${conversation.builder.group.size}")
    private int groupSize;
    @Value("${conversation.builder.messages}")
    private String messagesTextsString;
    private List<String> messagesTexts;
    private String randomMemeUrl;

    public void strikeUpConversations() {
        try {
            final List<UserDTO> randomUsers = userService.getRandomUsers();

            final List<String> randomUsersIds = randomUsers.stream()
                    .map(UserDTO::getId)
                    .collect(Collectors.toUnmodifiableList());

            batches(randomUsersIds, groupSize).forEach(this::tryToBuildConversation);

        } catch (CannotGetUsersException e) {
            log.error("Flow broken. Cannot get users: " + e);
        }
    }

    private void tryToBuildConversation(final List<String> batchedUsers) {
        try {
            log.info("Building conversation for: " + batchedUsers.toString());
            final String conversationId = createConversation(batchedUsers);
            getMessagesTexts().forEach(text -> this.tryToSendMessage(text, conversationId));
        } catch (RemoteCallException e) {
            log.error("Cannot create conversation for: " + batchedUsers + ". The reason is: " + e);
        }
    }

    private String createConversation(final List<String> usersIds) throws RemoteCallException {
        final ConversationRequestDTO conversationRequest = new ConversationRequestDTO(usersIds);
        return conversationRemoteClient.createConversation(conversationRequest);
    }

    private void tryToSendMessage(final String message, final String conversationId) {
        try {
            final MessageDTO messageDTO = new MessageDTO(conversationId, message);
            messageRemoteClient.sendMessage(messageDTO);
        } catch (RemoteCallException e) {
            log.error("Cannot send message to: " + conversationId + ". The reason is: " + e);
        }
    }

    private List<String> getMessagesTexts() {
        if (messagesTexts == null) {
            final String[] messagesArray = messagesTextsString.replace(MEME_URL_PLACEHOLDER, getRandomMemeUrl())
                    .split("\n");
            messagesTexts = Arrays.asList(messagesArray);
        }
        return messagesTexts;
    }

    private String getRandomMemeUrl() {
        if (randomMemeUrl == null) {
            try {
                randomMemeUrl = memeRemoteClient.getRandomMemeURL();
            } catch (RemoteCallException e) {
                log.error(e.getMessage());
                randomMemeUrl = "";
            }
        }
        return randomMemeUrl;
    }
}
