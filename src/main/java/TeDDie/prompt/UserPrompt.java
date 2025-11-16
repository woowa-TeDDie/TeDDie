package TeDDie.prompt;

import TeDDie.api.RagResult;
import TeDDie.domain.Difficulty;
import TeDDie.domain.Topic;
import java.util.List;

public class UserPrompt {
    private static final String TEMPLATE = """
            - 주제: %s
            - 난이도: %s
            """;

    private final String content;

    public UserPrompt(Topic topic, Difficulty difficulty, List<RagResult> ragResult) {
        this.content = String.format(
                TEMPLATE,
                topic.getValue(),
                difficulty.getValue(),
                ragResult
        );
    }

    public String getContent() {
        return content;
    }
}
