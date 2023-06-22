package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@MicronautTest
class MicrometerTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @ParameterizedTest
    @ValueSource(strings = {"microstream.main.globalFileCount","microstream.main.liveDataLength"})
    void microstreamMetrics(String metric) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = metricsRequest(metric);
        assertDoesNotThrow(() -> client.exchange(request));
    }

    private static HttpRequest<?> metricsRequest(String metric) {
        return HttpRequest.GET(metricsURI(metric));
    }

    private static URI metricsURI(String metric) {
        return UriBuilder.of("/metrics").path(metric).build();
    }
}
