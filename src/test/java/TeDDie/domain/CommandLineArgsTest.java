package TeDDie.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommandLineArgsTest {
    @DisplayName("올바른 인자로 CommandLineArgs 생성")
    @Test
    void 올바른_인자로_CommandLineArgs_생성() {
        //given
        String[] args = {"--topic", "random", "--difficulty", "easy"};

        //when
        CommandLineArgs commandLineArgs = new CommandLineArgs(args);

        //then
        assertThat(commandLineArgs.getTopic()).isEqualTo("random");
        assertThat(commandLineArgs.getDifficulty()).isEqualTo("easy");
    }
}
