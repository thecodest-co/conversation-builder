package co.thecodest.conversationbuilder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "external.service.slack.bot.base.url=https://example.com",
        "external.service.slack.bot.api.key=test-api-key",
        "conversation.builder.group.amount=3",
        "conversation.builder.group.size=3",
        "conversation.builder.messages=test"
})
class ConversationBuilderApplicationTests {

    @Test
    void contextLoads() {
    }

}
