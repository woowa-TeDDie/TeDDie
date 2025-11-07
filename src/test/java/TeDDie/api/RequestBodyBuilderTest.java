package TeDDie.api;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
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
        String systemPrompt = "시스템 테스트 프롬프트";
        String userPrompt = "유저 테스트 프롬프트";

        //when
        ApiRequest request = builder.buildRequestObject(systemPrompt, userPrompt);
        List<ApiMessage> messages = request.messages();

        //then
        assertThat(request.model()).isEqualTo("a.x-4.0-light");
        assertThat(request.temperature()).isEqualTo(0.7);
        assertThat(request.max_tokens()).isEqualTo(2000);

        ApiMessage systemMessage = messages.get(0);
        assertThat(systemMessage.role()).isEqualTo("system");
        assertThat(systemMessage.content()).isEqualTo(systemPrompt);

        ApiMessage userMessage = messages.get(1);
        assertThat(userMessage.role()).isEqualTo("user");
        assertThat(userMessage.content()).isEqualTo(userPrompt);
    }

    @DisplayName("요청 객체에 시스템과 유저 프롬프트를 추가하여 전송")
    @Test
    void 요청_객체에_시스템과_유저_프롬프트를_추가하여_전송() {
        //given
        String systemPrompt = "너는 TDD 튜터야";
        String userPrompt = "주제: collection";

        //when
        String result = builder.createJSONBody(systemPrompt, userPrompt);

        //then
        assertThat(result).contains("\"role\":\"system\"");
        assertThat(result).contains("\"content\":\"너는 TDD 튜터야\"");
        assertThat(result).contains("\"role\":\"user\"");
        assertThat(result).contains("\"content\":\"주제: collection\"");
        assertThat(result).contains("\"stream\":false");
    }
}
