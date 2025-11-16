package TeDDie.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

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
}
