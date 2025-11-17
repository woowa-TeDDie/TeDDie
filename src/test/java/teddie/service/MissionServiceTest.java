package teddie.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import teddie.common.util.HttpRequestSender;
import teddie.api.RagClient;
import teddie.api.dto.RagResult;
import teddie.domain.Difficulty;
import teddie.domain.Topic;
import java.util.List;
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
    private RagClient mockRagClient;

    @Mock
    private RequestBodyBuilder mockRequestBody;

    @InjectMocks
    private MissionService missionService;

    @DisplayName("API 응답을 파싱하여 실제 텍스트 반환")
    @Test
    void API_응답을_파싱하여_실제_텍스트_반환() throws Exception {
        //given
        Topic topic = new Topic("collection");
        Difficulty difficulty = Difficulty.EASY;
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

    @DisplayName("미션 생성 호출 시 system user 프롬프트를 분리하여 전달")
    @Test
    void 미션_생성_호출_시_올바른_프롬프트를_생성하여_전달() throws Exception {
        //given
        Topic topic = new Topic("collection");
        Difficulty difficulty = Difficulty.EASY;
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
        assertThat(actualSystemPrompt).contains("TDD");
        assertThat(actualSystemPrompt).contains("AI");
        assertThat(actualUserPrompt).contains("주제");
    }

    @DisplayName("RAG 검색 결과를 시스템 프롬프트에_포함")
    @Test
    void RAG_검색_결과를_시스템_프롬프트에_포함() throws Exception {
        //given
        Topic topic = new Topic("자동차 경주");
        Difficulty difficulty = Difficulty.EASY;
        List<RagResult> ragResults = List.of(
                new RagResult(
                        "java-racingcar-6",
                        "# 미션 - 자동차 경주\n초간단 자동차 경주 게임을 구현한다.",
                        "https://github.com/woowacourse-precourse/java-racingcar-6",
                        0.9
                )
        );
        when(mockRagClient.search(anyString(), anyInt()))
                .thenReturn(ragResults);

        when(mockRequestBody.createJSONBody(anyString(), anyString()))
                .thenReturn("{\"prompt\":\"...\"}");

        String testResponse = """
            {"choices":[{"message":{"content":"생성된 미션"}}]}
            """;
        when(mockSender.post(anyString(), anyString()))
                .thenReturn(testResponse);

        //when
        missionService.generateMission(topic, difficulty);

        //then
        ArgumentCaptor<String> systemPromptCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockRequestBody).createJSONBody(
                systemPromptCaptor.capture(),
                anyString()
        );
        String systemPrompt = systemPromptCaptor.getValue();
        assertThat(systemPrompt).contains("TDD");
        assertThat(systemPrompt).contains("TeDDie");
    }
}