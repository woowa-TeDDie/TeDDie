package TeDDie.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TopicTest {
    @DisplayName("Topic 생성")
    @Test
    void Topic_생성() {
        //given
        String input = "random";

        //when&then
        assertThatCode(() -> new Topic(input))
                .doesNotThrowAnyException();
    }

    @DisplayName("주제가 빈 입력값 일 때 예외 발생")
    @Test
    void 주제가_빈_입력값_일_때_예외_발생() {
        //given
        String input = "";

        //when&than
        assertThatThrownBy(() -> new Topic(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 주제는 빈 값일 수 없습니다.");
    }
}
