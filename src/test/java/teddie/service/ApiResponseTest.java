package teddie.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApiResponseTest {
    @DisplayName("정상적인 응답에서 content 추출")
    @Test
    void 정상적인_응답에서_content_추출() {
        //given
        Message message = new Message("미션 내용");
        Choice choice = new Choice(message);
        ApiResponse response = new ApiResponse(List.of(choice));

        //when
        String content = response.extractResponse();

        //then
        assertThat(content).isEqualTo("미션 내용");
    }

    @DisplayName("Choice가 비어있으면 예외 발생")
    @Test
    void choice가_비어있으면_예외_발생() {
        //given
        ApiResponse response = new ApiResponse(List.of());

        //when&then
        assertThatThrownBy(response::extractResponse)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]")
                .hasMessageContaining("choice");
    }

    @DisplayName("Message가 비어있으면 예외 발생")
    @Test
    void Message가_비어있으면_예외_발생() {
        //when&then
        assertThatThrownBy(() -> new Choice(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]")
                .hasMessageContaining("message");
    }
}
