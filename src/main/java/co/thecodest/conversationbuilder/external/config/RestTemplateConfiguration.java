package co.thecodest.conversationbuilder.external.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Value("${external.service.slack.bot.base.url}")
    private String slackBotBaseUrl;

    @Bean
    public RestTemplate slackBotApiRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.rootUri(slackBotBaseUrl).build();
    }
}
