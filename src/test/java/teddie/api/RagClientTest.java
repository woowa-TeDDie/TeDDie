package teddie.api;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teddie.api.dto.RagResult;
import teddie.common.util.HttpRequestSender;

public class RagClientTest {
    @DisplayName("RagClient 생성")
    @Test
    void RagClient_생성() {
        //given
        HttpRequestSender sender = new HttpRequestSender();

        //when
        RagClient ragClient = new RagClient(sender);

        //then
        assertThat(ragClient).isNotNull();
    }

    @DisplayName("빈 결과 리스트를 반환")
    @Test
    void 빈_결과_리스트를_반화() throws Exception {
        //given
        HttpRequestSender mockSender = mock(HttpRequestSender.class);
        String responseJson = """
            {
                "query": "테스트용검색어",
                "results": []
            }
            """;
        when(mockSender.post(anyString(), anyString()))
                .thenReturn(responseJson);
        RagClient ragClient = new RagClient(mockSender);

        //when
        List<RagResult> results = ragClient.search("테스트용검색어", 3);

        //then
        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(0);
    }

    @DisplayName("검색 결과 1개를 파싱하여 변환")
    @Test
    void 검색_결과_1개를_파싱하여_변환() throws Exception {
        //given
        HttpRequestSender mockSender = mock(HttpRequestSender.class);
        String responseJson = """
                {
                    "query": "자동차 경주",
                    "results": [
                        {
                            "repo": "java-racingcar-6",
                            "text": "자동차 경주",
                            "url": "https://github.com/woowacourse-precourse/java-racingcar-6",
                            "similarity_score": 0.85
                        }
                    ]
                }
                """;
        when(mockSender.post(anyString(), anyString()))
                .thenReturn(responseJson);
        RagClient ragClient = new RagClient(mockSender);

        //when
        List<RagResult> results = ragClient.search("자동차 경주", 3);

        //then
        assertThat(results.size()).isEqualTo(1);
        RagResult ragResult = results.get(0);
        assertThat(ragResult.repo()).isEqualTo("java-racingcar-6");
        assertThat(ragResult.similarityScore()).isEqualTo(0.85);
    }

    @DisplayName("검색 결과 여러 개를 파싱하여 반환")
    @Test
    void 검색_결과_여러_개를_파싱하여_반환() throws Exception {
        //given
        HttpRequestSender mockSender = mock(HttpRequestSender.class);
        String responseJson = """
            {
                "query": "TDD",
                "results": [
                    {
                        "repo": "java-racingcar-6",
                        "text": "Text 1",
                        "url": "https://url1.com",
                        "similarity_score": 0.9
                    },
                    {
                        "repo": "java-lotto-6",
                        "text": "Text 2",
                        "url": "https://url2.com",
                        "similarity_score": 0.7
                    },
                    {
                        "repo": "java-baseball-6",
                        "text": "Text 3",
                        "url": "https://url3.com",
                        "similarity_score": 0.5
                    }
                ]
            }
            """;
        when(mockSender.post(anyString(), anyString()))
                .thenReturn(responseJson);
        RagClient ragClient = new RagClient(mockSender);

        //when
        List<RagResult> results = ragClient.search("TDD", 3);

        //then
        assertThat(results.size()).isEqualTo(3);
        assertThat(results.get(0).repo()).isEqualTo("java-racingcar-6");
        assertThat(results.get(1).repo()).isEqualTo("java-lotto-6");
        assertThat(results.get(2).repo()).isEqualTo("java-baseball-6");
    }
}