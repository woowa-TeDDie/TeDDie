package teddie.service;

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
    Choice {
        validateEmpty(message);
    }

    private void validateEmpty(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("[ERROR] API 응답에 message가 없습니다.");
        }
    }

    String extractMessage() {
        return message.extractContent();
    }
}

record Message(String content) {
    String extractContent() {
        return content;
    }
}
