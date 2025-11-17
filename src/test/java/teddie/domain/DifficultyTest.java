package teddie.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DifficultyTest {
    @DisplayName("easy_문자열로_쉬움_난이도_생성")
    @Test
    void easy_문자열로_쉬움_난이도_생성() {
        //given
        String input = "easy";

        //when
        Difficulty difficulty = Difficulty.from(input);

        //then
        assertThat(difficulty).isEqualTo(Difficulty.EASY);
    }

    @DisplayName("medium_문자열로_중간_난이도_생성")
    @Test
    void medium_문자열로_쉬움_난이도_생성() {
        //given
        String input = "medium";

        //when
        Difficulty difficulty = Difficulty.from(input);

        //then
        assertThat(difficulty).isEqualTo(Difficulty.MEDIUM);
    }

    @DisplayName("hard_문자열로_어려움_난이도_생성")
    @Test
    void hard_문자열로_쉬움_난이도_생성() {
        //given
        String input = "HARD";

        //when
        Difficulty difficulty = Difficulty.from(input);

        //then
        assertThat(difficulty).isEqualTo(Difficulty.HARD);
    }

    @DisplayName("유효하지 않은 값 입력 시 예외 발생")
    @Test
    void 유효하지_않은_값_입력_시_예외_발생() {
        //given
        String input = "veryHard";

        //when&then
        assertThatThrownBy(() -> Difficulty.from(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 유효하지 않은 난이도입니다.");
    }

    @DisplayName("빈 값 입력 시 예외 발생")
    @Test
    void 빈_값_입력_시_예외_발생() {
        //given
        String input = "";

        //when&then
        assertThatThrownBy(() -> Difficulty.from(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 난이도는 빈 문자열일 수 없습니다.");
    }
}
