package co.thecodest.conversationbuilder.external.config;

import com.amazonaws.DefaultRequest;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.HttpMethodName;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.amazonaws.regions.Regions.US_EAST_1;

public class RestTemplateHeaderModifierInterceptor
        implements ClientHttpRequestInterceptor {

    public static Map<String, String> aws4Sign(URI uri, String methodName) throws URISyntaxException {
        Map<String, String> headers = new HashMap<>();
        String serviceName = "execute-api";
        AWS4Signer aws4Signer = new AWS4Signer();
        aws4Signer.setRegionName(US_EAST_1.getName());
        aws4Signer.setServiceName(serviceName);
        DefaultRequest defaultRequest = new DefaultRequest(serviceName);
        defaultRequest.setEndpoint(new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), "", "", ""));
        defaultRequest.setHttpMethod(HttpMethodName.fromValue(methodName));
        defaultRequest.setResourcePath(uri.getRawPath());
        defaultRequest.setHeaders(headers);
        AWSCredentials credentials = DefaultAWSCredentialsProviderChain.getInstance().getCredentials();
        System.out.println(credentials.getAWSAccessKeyId());
        System.out.println(credentials.getAWSSecretKey());
        aws4Signer.sign(defaultRequest, credentials);
        return defaultRequest.getHeaders();
    }

    @SneakyThrows
    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        final HttpHeaders headers = request.getHeaders();
        final String methodName = request.getMethod().name();
        final URI uri = request.getURI();
        final Map<String, String> headersMap = aws4Sign(uri, methodName);
        headersMap.forEach(headers::add);
        return execution.execute(request, body);
    }
}
