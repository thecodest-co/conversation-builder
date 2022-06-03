package co.thecodest.conversationbuilder.external.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SlackRestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor {

    private static final String X_API_KEY_HEADER = "x-api-key";
    @Value("${external.service.slack.bot.api.key}")
    private String slackApiKey;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        final HttpHeaders headers = request.getHeaders();
        headers.add(X_API_KEY_HEADER, slackApiKey);
        return execution.execute(request, body);
    }
}
