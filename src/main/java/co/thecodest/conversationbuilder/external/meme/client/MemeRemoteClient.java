package co.thecodest.conversationbuilder.external.meme.client;

import co.thecodest.conversationbuilder.external.meme.dto.MemeResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class MemeRemoteClient {

    @Value("${external.service.meme.url}")
    private String memeServiceUrl;

    private final RestTemplate restTemplate;

    public MemeRemoteClient(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    public String getRandomMemeURL() {
        try {
            final MemeResponseDTO response = restTemplate.getForObject(memeServiceUrl, MemeResponseDTO.class);
            log.info("Meme Rest Api call success: " + response);
            if (response != null && response.getImage() != null) {
                return response.getImage();
            }
        } catch (RestClientException e) {
            log.error("Meme Rest Api call failure. Reason: \n" + e);
        }
        return "";
    }
}
