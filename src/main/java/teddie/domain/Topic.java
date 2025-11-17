package teddie.domain;

public class Topic {
    private final String value;

    public Topic(String value) {
        validateEmptyInput(value);
        this.value = value;
    }

    private void validateEmptyInput(String value) {
        if(value == null || value.isBlank()) {
            throw new IllegalArgumentException("주제는 빈 값일 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
