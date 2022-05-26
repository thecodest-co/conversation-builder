package co.thecodest.conversationbuilder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"external.api.base.url=https://example.com"})
class ConversationBuilderApplicationTests {

    @Test
    void contextLoads() {
    }

}
