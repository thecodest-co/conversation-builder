package co.thecodest.conversationbuilder.external.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfiguration {

    @Value("${external.service.slack.bot.base.url}")
    private String slackBotBaseUrl;

    private final SlackRestTemplateHeaderModifierInterceptor interceptor;

    @Bean
    public RestTemplate slackBotApiRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        final RestTemplate restTemplate = restTemplateBuilder.rootUri(slackBotBaseUrl).build();
        addInterceptor(restTemplate);
        return restTemplate;
    }

    private void addInterceptor(RestTemplate restTemplate) {
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(interceptor);
        restTemplate.setInterceptors(interceptors);
    }

}
