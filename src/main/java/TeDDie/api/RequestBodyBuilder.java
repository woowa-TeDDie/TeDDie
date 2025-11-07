package TeDDie.api;

import com.google.gson.Gson;
import java.util.List;

public class RequestBodyBuilder {
    private static final String DEFAULT_MODEL = "a.x-4.0-light";
    private static final String SYSTEM_ROLE = "system";
    private static final String USER_ROLE = "user";
    private static final double DEFAULT_TEMPERATURE = 0.7;
    private static final int DEFAULT_MAX_TOKENS = -1;
    private static final boolean DEFAULT_STREAM = false;

    private final Gson gson;

    public RequestBodyBuilder() {
        this.gson = new Gson();
    }

    public ApiRequest buildRequestObject(String systemPrompt, String userPrompt) {
        ApiMessage systemMessage = new ApiMessage(SYSTEM_ROLE, systemPrompt);
        ApiMessage userMessage = new ApiMessage(USER_ROLE, userPrompt);
        return new ApiRequest(
                DEFAULT_MODEL,
                List.of(systemMessage, userMessage),
                DEFAULT_TEMPERATURE,
                DEFAULT_MAX_TOKENS,
                DEFAULT_STREAM
        );
    }

    public String createJSONBody(String systemPrompt, String userPrompt) {
        ApiRequest request = buildRequestObject(systemPrompt, userPrompt);
        return gson.toJson(request);
    }
}
