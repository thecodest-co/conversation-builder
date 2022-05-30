package co.thecodest.conversationbuilder.cloudfunction;

import co.thecodest.conversationbuilder.ConversationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AwsFunction implements Function<String, String> {

    private final ConversationFacade conversationFacade;

    @Override
    public String apply(String input) {
        try {
            conversationFacade.strikeUpConversations();
            return "Success";
        } catch (Exception e) {
            return "Execution failure. Reason: \n" + e.getMessage();
        }
    }

}
