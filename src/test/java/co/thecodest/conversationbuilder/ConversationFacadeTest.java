package co.thecodest.conversationbuilder;

import co.thecodest.conversationbuilder.conversation.client.ConversationClient;
import co.thecodest.conversationbuilder.meme.client.MemeClient;
import co.thecodest.conversationbuilder.message.client.MessageClient;
import co.thecodest.conversationbuilder.message.dto.MessageDTO;
import co.thecodest.conversationbuilder.user.dto.UserDTO;
import co.thecodest.conversationbuilder.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static co.thecodest.conversationbuilder.TestUtils.createUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationFacadeTest {

    private static final String MESSAGES = "Message1\nMessage2 with <meme-url>\nMessage3";
    private static final int NUMBER_OF_GROUPS = 2;
    private static final int GROUPS_SIZE = 2;
    private static final long NUMBER_OF_USERS = NUMBER_OF_GROUPS * GROUPS_SIZE;
    private static final String MEME_URL = "MEME-URL";
    private static final String MEME_URL_PLACEHOLDER = "<meme-url>";
    @Mock
    private UserService userService;
    @Mock
    private ConversationClient conversationClient;
    @Mock
    private MemeClient memeClient;
    @Mock
    private MessageClient messageClient;
    @InjectMocks
    private ConversationFacade conversationFacade;
    @Captor
    private ArgumentCaptor<MessageDTO> messageArgumentCaptor;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(conversationFacade, "numberOfGroups", NUMBER_OF_GROUPS);
        ReflectionTestUtils.setField(conversationFacade, "groupSize", GROUPS_SIZE);
        ReflectionTestUtils.setField(conversationFacade, "messagesString", MESSAGES);
    }

    @Test
    void facadeSuccessfullyStrokeUpConversations() {
        final List<UserDTO> users = createUsers(((int) NUMBER_OF_USERS));
        when(userService.getRandomUsersUpToLimit(NUMBER_OF_USERS)).thenReturn(users);
        when(memeClient.getRandomMemeURL()).thenReturn(MEME_URL);

        conversationFacade.strikeUpConversations();

        verify(userService, times(1)).getRandomUsersUpToLimit(eq(NUMBER_OF_USERS));
        verify(memeClient, times(1)).getRandomMemeURL();
        verify(conversationClient, times(NUMBER_OF_GROUPS)).createConversation(any());
        verify(messageClient, atLeastOnce()).sendMessage(messageArgumentCaptor.capture());

        assertThatAllMessagesSend();
    }

    private void assertThatAllMessagesSend() {
        final String[] messagesToSend = MESSAGES.replace(MEME_URL_PLACEHOLDER, MEME_URL).split("\n");
        final List<String> sendMessages = messageArgumentCaptor.getAllValues().stream()
                .map(MessageDTO::getText)
                .collect(Collectors.toList());
        assertThat(sendMessages).containsAll(Arrays.asList(messagesToSend));
    }

}