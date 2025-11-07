package TeDDie.service;

import TeDDie.api.ApiRequest;
import TeDDie.api.HttpRequestSender;
import TeDDie.api.RequestBodyBuilder;
import com.google.gson.Gson;

public class MissionService {
    private final HttpRequestSender sender;
    private final RequestBodyBuilder builder;
    private final Gson gson;

    public MissionService(HttpRequestSender sender, RequestBodyBuilder builder) {
        this.sender = sender;
        this.builder = builder;
        this.gson = new Gson();
    }

    public String generateMission(String topic, String difficulty) throws Exception {
        String prompt = String.format("주제: %s, 난이도 %s", topic, difficulty);
        String requestJson = builder.createJSONBody(prompt);
        String responseJson = sender.post("https://localhost:1234/v1/", requestJson);
        return parseContentFromResponse(responseJson);
    }

    public String parseContentFromResponse(String responseJson) {
        ApiResponse response = gson.fromJson(responseJson, ApiResponse.class);
        return response.choices().get(0).message().content();
    }
}
