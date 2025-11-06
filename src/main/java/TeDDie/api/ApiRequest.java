package TeDDie.api;

import java.util.List;

public record ApiRequest(
        String model,
        List<ApiMessage> messages,
        double temperature,
        int max_tokens
) {
}
