package TeDDie.service;

import java.util.List;

record ApiResponse(List<Choice> choices) {}

record Choice(Message message) {}

record Message(String content) {}
