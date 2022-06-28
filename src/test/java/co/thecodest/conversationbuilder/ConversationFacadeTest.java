package co.thecodest.conversationbuilder;

import co.thecodest.conversationbuilder.external.conversation.client.ConversationRemoteClient;
import co.thecodest.conversationbuilder.external.meme.client.MemeRemoteClient;
import co.thecodest.conversationbuilder.external.message.client.MessageRemoteClient;
import co.thecodest.conversationbuilder.external.message.dto.MessageDTO;
import co.thecodest.conversationbuilder.external.user.dto.UserDTO;
import co.thecodest.conversationbuilder.external.user.service.UserService;
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

import static co.thecodest.conversationbuilder.TestUtil.createUsers;
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
    private ConversationRemoteClient conversationRemoteClient;
    @Mock
    private MemeRemoteClient memeRemoteClient;
    @Mock
    private MessageRemoteClient messageRemoteClient;
    @InjectMocks
    private ConversationFacade conversationFacade;
    @Captor
    private ArgumentCaptor<MessageDTO> messageArgumentCaptor;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(conversationFacade, "groupSize", GROUPS_SIZE);
        ReflectionTestUtils.setField(conversationFacade, "messagesTextsString", MESSAGES);
    }

    @Test
    void facadeSuccessfullyStrokeUpConversations() {
        final List<UserDTO> users = createUsers(((int) NUMBER_OF_USERS));
        when(userService.getRandomUsers()).thenReturn(users);
        when(memeRemoteClient.getRandomMemeURL()).thenReturn(MEME_URL);

        conversationFacade.strikeUpConversations();

        verify(userService, times(1)).getRandomUsers();
        verify(memeRemoteClient, times(1)).getRandomMemeURL();
        verify(conversationRemoteClient, times(NUMBER_OF_GROUPS)).createConversation(any());
        verify(messageRemoteClient, atLeastOnce()).sendMessage(messageArgumentCaptor.capture());

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
