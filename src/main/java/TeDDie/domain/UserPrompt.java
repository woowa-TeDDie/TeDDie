package TeDDie.domain;

public class UserPrompt {
    private final String content;

    public UserPrompt(Topic topic, Difficulty difficulty) {
        this.content = String.format(
                topic.getValue(),
                difficulty.getValue()
        );
    }

}
