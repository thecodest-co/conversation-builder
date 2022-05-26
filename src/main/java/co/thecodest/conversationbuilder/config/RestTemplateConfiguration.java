package co.thecodest.conversationbuilder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Value("${external.api.base.url}")
    private String externalApiBaseUrl;

    @Bean
    public RestTemplate slackApiRestTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.rootUri(externalApiBaseUrl).build();
    }
}
