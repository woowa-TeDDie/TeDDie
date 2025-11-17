package teddie.service;

record Choice(Message message) {
    Choice {
        validateEmpty(message);
    }

    private void validateEmpty(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("API 응답에 message가 없습니다.");
        }
    }

    String extractMessage() {
        return message.extractContent();
    }
}
