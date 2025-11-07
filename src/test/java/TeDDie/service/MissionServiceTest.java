package TeDDie.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import TeDDie.api.HttpRequestSender;
import TeDDie.api.RequestBodyBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        when(mockRequestBody.createJSONBody(anyString()))
                .thenReturn("{\"promt\":\"...\"}");
        when(mockSender.post(anyString(), anyString()))
                .thenReturn(testResponse);

        String actualText = missionService.generateMission(topic, difficulty);

        //then
        assertThat(actualText).isEqualTo(result);
    }
}
