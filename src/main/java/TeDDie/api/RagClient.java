package TeDDie.api;

import com.google.gson.Gson;

public class RagClient {
    private final HttpRequestSender sender;
    private final Gson gson;

    public RagClient(HttpRequestSender sender) {
        this.sender = sender;
        this.gson = new Gson();
    }
}
