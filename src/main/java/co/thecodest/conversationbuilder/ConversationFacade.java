package co.thecodest.conversationbuilder;

import co.thecodest.conversationbuilder.conversation.client.ConversationClient;
import co.thecodest.conversationbuilder.conversation.dto.ConversationRequestDTO;
import co.thecodest.conversationbuilder.meme.client.MemeClient;
import co.thecodest.conversationbuilder.message.client.MessageClient;
import co.thecodest.conversationbuilder.message.dto.MessageDTO;
import co.thecodest.conversationbuilder.user.dto.UserDTO;
import co.thecodest.conversationbuilder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static co.thecodest.conversationbuilder.util.ListUtil.batches;

@Component
@RequiredArgsConstructor
public class ConversationFacade {

    private static final String MEME_URL_PLACEHOLDER = "<meme-url>";
    private final UserService userService;
    private final ConversationClient conversationClient;
    private final MemeClient memeClient;
    private final MessageClient messageClient;
    @Value("${conversation.builder.group.amount}")
    private int numberOfGroups;
    @Value("${conversation.builder.group.size}")
    private int groupSize;
    @Value("${conversation.builder.messages}")
    private String messagesString;
    private List<String> messages;
    private String randomMemeUrl;

    public void strikeUpConversations() {
        int numberOfUsers = numberOfGroups * groupSize;
        final List<String> randomUsersIds = userService.getRandomUsersUpToLimit(numberOfUsers).stream()
                .map(UserDTO::getId)
                .collect(Collectors.toUnmodifiableList());

        batches(randomUsersIds, groupSize)
                .map(this::createConversation)
                .forEach(conversationId -> {
                    getMessages().forEach(message -> this.sendMessage(message, conversationId));
                });
    }

    private String createConversation(final List<String> usersIds) {
        final ConversationRequestDTO conversationRequest = new ConversationRequestDTO(usersIds);
        return conversationClient.createConversation(conversationRequest);
    }

    private void sendMessage(final String message, final String conversationId) {
        final MessageDTO messageDTO = new MessageDTO(conversationId, message);
        messageClient.sendMessage(messageDTO);
    }

    private List<String> getMessages() {
        if (messages == null) {
            final String[] messagesArray = messagesString.replace(MEME_URL_PLACEHOLDER, getRandomMemeUrl())
                    .split("\n");
            messages = Arrays.asList(messagesArray);
        }
        return messages;
    }

    private String getRandomMemeUrl() {
        if (randomMemeUrl == null) {
            randomMemeUrl = memeClient.getRandomMemeURL();
        }
        return randomMemeUrl;
    }
}
