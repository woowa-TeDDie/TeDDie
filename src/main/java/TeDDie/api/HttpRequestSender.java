package TeDDie.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpRequestSender {
    private final HttpClient httpClient;

    public HttpRequestSender() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String post(String url, String requestBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest
                        .BodyPublishers
                        .ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers
                .ofString());

        validateStatus(response);
        return response.body();
    }

    private void validateStatus(HttpResponse<String> response) {
        int statusCode = response.statusCode();

        if(statusCode < 200 || statusCode >= 300) {
            throw new RuntimeException("HTTP Error Status: " + statusCode);
        }
    }
}
