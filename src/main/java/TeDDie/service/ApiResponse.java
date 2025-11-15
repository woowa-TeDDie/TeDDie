package TeDDie.service;

import java.util.List;

record ApiResponse(List<Choice> choices) {
    String extractResponse() {
        validateEmpty();
        return choices.get(0).extractMessage();
    }

    private void validateEmpty() {
        if (choices == null || choices.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] API 응답에 choice가 없습니다.");
        }
    }
}

record Choice(Message message) {
    String extractMessage() {
        return message.extractContent();
    }
}

record Message(String content) {
    String extractContent() {
        return content;
    }
}
