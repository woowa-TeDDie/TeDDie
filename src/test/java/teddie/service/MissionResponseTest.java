package teddie.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MissionResponseTest {
    @DisplayName("미션과 테스트 케이스를 포함하는 MissionResponse 생성")
    @Test
    void 미션과_테스트_케이스를_포함하는_MissionResponse_생성() {
        //given
        String mission = "# 🧩 로또";
        List<TestCase> testCases = List.of(
                new TestCase("기능_테스트", "기능 테스트", "1000\\n", "[1, 2, 3]", false)
        );

        //when&then
        assertThatCode(() -> new MissionResponse(mission, testCases))
                .doesNotThrowAnyException();
    }
}
