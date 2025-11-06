package TeDDie.api;

import java.util.List;

public class RequestBodyBuilder {
    private static final String DEFAULT_MODEL = "a.x-4.0-light";
    private static final String USER_ROLE = "user";
    private static final double DEFAULT_TEMPERATURE = 0.7;
    private static final int DEFAULT_MAX_TOKENS = 2000;

    public ApiRequest buildRequestObject(String prompt) {
        ApiMessage message = new ApiMessage(USER_ROLE, prompt);
        return new ApiRequest(
                DEFAULT_MODEL,
                List.of(message),
                DEFAULT_TEMPERATURE,
                DEFAULT_MAX_TOKENS
        );
    }
}
