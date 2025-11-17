package teddie.common.util;

import teddie.exception.HttpRequestException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpRequestSender {
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_APPLICATION_JSON = "application/json";

    private final HttpClient httpClient;

    public HttpRequestSender() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String post(String url, String requestBody) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header(HEADER_CONTENT_TYPE, HEADER_APPLICATION_JSON)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            validateStatus(response);
            return response.body();
        } catch (IOException e) {
            throw new HttpRequestException("HTTP 요청 중 IO 오류 발생: " + url, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HttpRequestException("HTTP 요청이 중단되었습니다: " + url, e);
        }
    }

    private void validateStatus(HttpResponse<String> response) {
        int statusCode = response.statusCode();

        if (statusCode < 200 || statusCode >= 300) {
            throw new HttpRequestException("HTTP 에러 상태: " + statusCode);
        }
    }
}