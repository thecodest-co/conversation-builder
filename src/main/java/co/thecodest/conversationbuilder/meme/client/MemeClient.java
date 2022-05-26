package co.thecodest.conversationbuilder.meme.client;

import co.thecodest.conversationbuilder.meme.dto.MemeResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class MemeClient {

    private static final String EMPTY_STRING = "";

    @Value("${external.api.meme.url}")
    private String memeUrl;

    private final RestTemplate restTemplate;

    public MemeClient(RestTemplateBuilder builder) {
        restTemplate = builder.rootUri(memeUrl).build();
    }

    public String getRandomMemeURL() {
        try {
            final MemeResponseDTO response = restTemplate.getForObject("", MemeResponseDTO.class);
            log.info("Meme Rest Api call success: " + response);
            if (response != null && response.getImage() != null) {
                return response.getImage();
            }
        } catch (RestClientException e) {
            log.error("Meme Rest Api call failure. Reason: \n" + e);
        }
        return EMPTY_STRING;
    }
}
