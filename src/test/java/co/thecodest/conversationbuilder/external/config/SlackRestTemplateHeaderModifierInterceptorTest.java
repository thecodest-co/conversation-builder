package co.thecodest.conversationbuilder.external.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SlackRestTemplateHeaderModifierInterceptorTest {

    private static final String TEST_API_KEY = "TEST-API-KEY";

    private static final String X_API_KEY_HEADER = "x-api-key";

    @InjectMocks
    private SlackRestTemplateHeaderModifierInterceptor interceptor;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(interceptor, "slackApiKey", TEST_API_KEY);
    }

    @Test
    public void interceptorSuccessfullyAddsApiKeyHeader() throws IOException {
        final HttpRequest request = new TestHttpRequest();
        byte[] body = {};
        final ClientHttpRequestExecution execution = (request1, body1) -> null;

        interceptor.intercept(request, body, execution);

        assertTrue(request.getHeaders().containsKey(X_API_KEY_HEADER));
        assertThat(request.getHeaders().get(X_API_KEY_HEADER)).contains(TEST_API_KEY);
    }

    static class TestHttpRequest implements HttpRequest {

        private final HttpHeaders headers = new HttpHeaders();

        @Override
        public String getMethodValue() {
            return null;
        }

        @Override
        public URI getURI() {
            return null;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }

}