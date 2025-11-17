package teddie.domain;

import java.util.Arrays;

public enum Difficulty {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard");

    private final String value;

    Difficulty(String value) {
        this.value = value;
    }

    public static Difficulty from(String input) {
        validateEmptyInput(input);

        return Arrays.stream(Difficulty.values())
                .filter(difficulty -> difficulty.value.equals(input.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "[ERROR] 유효하지 않은 난이도입니다."
                ));
    }

    private static void validateEmptyInput(String input) {
        if (input.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 난이도는 빈 문자열일 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
