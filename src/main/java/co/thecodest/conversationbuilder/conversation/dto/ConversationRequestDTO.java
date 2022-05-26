package co.thecodest.conversationbuilder.conversation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationRequestDTO {
   private List<String> users;
}
