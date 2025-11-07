package TeDDie.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import TeDDie.api.HttpRequestSender;
import TeDDie.api.RequestBodyBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MissionServiceTest {
    @Mock
    private HttpRequestSender mockSender;

    @Mock
    private RequestBodyBuilder mockRequestBody;

    @InjectMocks
    private MissionService missionService;

    @DisplayName("API 응답을 파싱하여 실제 텍스트 반환")
    @Test
    void API_응답을_파싱하여_실제_텍스트_반환() throws Exception {
        //given
        String topic = "collection";
        String difficulty = "easy";
        String testResponse = """
                {
                    "choices": [
                        {
                            "message": {
                                "content": "## 미션: 문자열 계산기"
                            }
                        }
                    ]
                }
                """;
        String result = "## 미션: 문자열 계산기";

        //when
        when(mockRequestBody.createJSONBody(anyString(), anyString()))
                .thenReturn("{\"promt\":\"...\"}");
        when(mockSender.post(anyString(), anyString()))
                .thenReturn(testResponse);

        String actualText = missionService.generateMission(topic, difficulty);

        //then
        assertThat(actualText).isEqualTo(result);
    }

    @DisplayName("미션 생성 호출 시 system/user 프롬프트를 분리하여 전달")
    @Test
    void 미션_생성_호출_시_올바른_프롬프트를_생성하여_전달() throws Exception {
        //given
        String topic = "collection";
        String difficulty = "easy";
        when(mockRequestBody.createJSONBody(anyString(), anyString()))
                .thenReturn("{\"promt\":\"...\"}");

        String testResponse = """
                { "choices": [{"message": {"content": "Test Content"}}]}
                """;

        when(mockSender.post(anyString(), anyString()))
                .thenReturn(testResponse);

        ArgumentCaptor<String> systemPromptCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> userPromptCaptor = ArgumentCaptor.forClass(String.class);

        //when
        missionService.generateMission(topic, difficulty);

        //then
        verify(mockRequestBody).createJSONBody(
                systemPromptCaptor.capture(),
                userPromptCaptor.capture()
        );
        String actualSystemPrompt = systemPromptCaptor.getValue();
        String actualUserPrompt = userPromptCaptor.getValue();
        assertThat(actualSystemPrompt).contains("TDD 연습");
        assertThat(actualSystemPrompt).contains("AI 튜터");
        assertThat(actualUserPrompt).contains("주제: collection");
        assertThat(actualUserPrompt).contains("난이도: easy");
        assertThat(actualUserPrompt).doesNotContain("TDD 연습");
    }
}