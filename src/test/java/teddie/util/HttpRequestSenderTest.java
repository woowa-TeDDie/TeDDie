package teddie.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.io.IOException;
import java.net.ConnectException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teddie.common.util.HttpRequestSender;

public class HttpRequestSenderTest {
    private MockWebServer mockWebServer;
    private HttpRequestSender sender;

    @BeforeEach
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        sender = new HttpRequestSender();
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @DisplayName("서버가 200 OK 응답시 응답 본문을 그대로 반환")
    @Test
    void 서버가_200_OK_응답시_응답_본문을_그대로_반환() throws Exception {
        //given
        String expectedBody = "{\"resqonse\":\"목 테스트 성공\"}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(expectedBody));
        String mockUrl = mockWebServer.url("/").toString();
        String dummyRequestBody = "{}";

        //when
        String result = sender.post(mockUrl, dummyRequestBody);

        //then
        assertThat(result).isEqualTo(expectedBody);
    }

    @DisplayName("서버가 500 에러 응답시 RuntimeException을 던짐")
    @Test
    void 서버가_500_에러_응답시_RuntimeException을_던짐() {
        //given
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
        );
        String mockUrl = mockWebServer.url("/").toString();
        String dummyRequestBody = "{}";

        //when&then
        assertThatThrownBy(() -> sender.post(mockUrl, dummyRequestBody))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("500");
    }

    @DisplayName("POST 요청 시 올바른 메서드, 헤더, 본문 전송")
    @Test
    void POST_요청_시_올바른_메서드_헤더_본문_전송() throws Exception {
        //given
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"response\":\"ok\"}")
        );
        String mockUrl = mockWebServer.url("/").toString();
        String input = "{\"prompt\":\"this is body\"}";

        //when
        sender.post(mockUrl, input);

        //then
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getHeader("Content-Type")).isEqualTo("application/json");
        assertThat(request.getBody().readUtf8()).isEqualTo(input);
    }

    @DisplayName("연결할 수 없는 주소로 요청시 ConnectException 던짐")
    @Test
    void 연결할_수_없는_주소로_요청시_ConnectException을_던짐() {
        //given
        String url = "http://localhost:9000/test-path";
        String input = "{}";

        //when&then
        assertThatThrownBy(() -> sender.post(url, input))
                .isInstanceOf(ConnectException.class);
    }
}
