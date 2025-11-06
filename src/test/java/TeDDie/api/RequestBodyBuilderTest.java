package TeDDie.api;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RequestBodyBuilderTest {
    private RequestBodyBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new RequestBodyBuilder();
    }

    @DisplayName("프롬프트를 API 요청 객체로 변환")
    @Test
    void 프롬프트를_API_요청_객체로_변환() {
        //given
        String prompt = "테스트 프롬프트";

        //when
        ApiRequest request = builder.buildRequestObject(prompt);

        //then
        assertThat(request.model()).isEqualTo("a.x-4.0-light");
        assertThat(request.temperature()).isEqualTo(0.7);
        assertThat(request.max_tokens()).isEqualTo(2000);

        ApiMessage message = request.messages().get(0);
        assertThat(message.role()).isEqualTo("user");
        assertThat(message.content()).isEqualTo(prompt);
    }
}
