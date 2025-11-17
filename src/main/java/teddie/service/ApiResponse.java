package teddie.service;

import java.util.List;

record ApiResponse(List<Choice> choices) {
    String extractResponse() {
        validateEmpty();
        return choices.get(0).extractMessage();
    }

    private void validateEmpty() {
        if (choices == null || choices.isEmpty()) {
            throw new IllegalArgumentException("API 응답에 choice가 없습니다.");
        }
    }
}
