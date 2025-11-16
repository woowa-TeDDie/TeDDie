package TeDDie.service;

import static TeDDie.prompt.SystemPrompt.SYSTEM_PROMPT;

import TeDDie.api.HttpRequestSender;
import TeDDie.api.RagClient;
import TeDDie.api.RagResult;
import TeDDie.api.RequestBodyBuilder;
import TeDDie.domain.Difficulty;
import TeDDie.domain.Topic;
import TeDDie.prompt.UserPrompt;
import com.google.gson.Gson;
import java.util.List;

public class MissionService {
    private final HttpRequestSender sender;
    private final RequestBodyBuilder builder;
    private final RagClient ragClient;

    public MissionService(HttpRequestSender sender, RequestBodyBuilder builder, RagClient ragClient) {
        this.sender = sender;
        this.builder = builder;
        this.ragClient = ragClient;
    }

    public String generateMission(Topic topic, Difficulty difficulty) throws Exception {
        List<RagResult> ragResults = ragClient.search(topic.getValue(), 3);
        UserPrompt userPrompt = new UserPrompt(topic, difficulty, ragResults);
        String requestJson = builder.createJSONBody(SYSTEM_PROMPT, userPrompt.getContent());
        String responseJson = sender.post("http://localhost:1234/v1/chat/completions", requestJson);
        return parseContentFromResponse(responseJson);
    }

    public String parseContentFromResponse(String responseJson) {
        Gson gson = new Gson();
        ApiResponse response = gson.fromJson(responseJson, ApiResponse.class);
        return response.extractResponse();
    }
}