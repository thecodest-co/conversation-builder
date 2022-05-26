package co.thecodest.conversationbuilder.meme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemeResponseDTO {
    private Long id;
    private String image;
    private String caption;
    private String category;
}
