package co.thecodest.conversationbuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class ConversationBuilderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConversationBuilderApplication.class, args);
    }

    @Autowired
    private ConversationFacade conversationFacade;

    @Bean
    public Function<String, String> awsFunction2() {
        conversationFacade.strikeUpConversations();
        return (input) -> ("SUC");
    }
}
