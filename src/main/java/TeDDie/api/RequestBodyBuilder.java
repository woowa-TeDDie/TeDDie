package TeDDie.api;

import java.util.List;

public class RequestBodyBuilder {
    public ApiRequest buildRequestObject(String prompt) {
        ApiMessage message = new ApiMessage("user", prompt);
        return new ApiRequest(
                "a.x-4.0-light",
                List.of(message),
                0.7,
                2000
        );
    }
}
