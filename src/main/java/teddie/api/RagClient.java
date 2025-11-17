package teddie.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import teddie.api.dto.RagResult;
import teddie.common.util.HttpRequestSender;

public class RagClient {
    private static final String RAG_API_URL = "http://localhost:8000/api/search";
    private static final String QUERY = "query";
    private static final String TOP_K = "top_k";
    private static final String RESULTS = "results";
    private static final String REPO =  "repo";
    private static final String TEXT = "text";
    private static final String URL = "url";
    private static final String SIMILARITY_SCORE = "similarity_score";

    private final HttpRequestSender sender;
    private final Gson gson;

    public RagClient(HttpRequestSender sender) {
        this.sender = sender;
        this.gson = new Gson();
    }

    public List<RagResult> search(String query, int topK) throws Exception {
        String requestBody = buildSearchRequest(query, topK);
        String responseJson = sender.post(RAG_API_URL, requestBody);
        return parseSearchResponse(responseJson);
    }

    private String buildSearchRequest(String query, int topK) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty(QUERY, query);
        requestBody.addProperty(TOP_K, topK);
        return gson.toJson(requestBody);
    }

    private List<RagResult> parseSearchResponse(String responseJson) {
        JsonObject response = gson.fromJson(responseJson, JsonObject.class);
        JsonArray results = response.getAsJsonArray(RESULTS);

        List<RagResult> ragResults = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            JsonObject item = results.get(i).getAsJsonObject();
            ragResults.add(parseItem(item));
        }
        return ragResults;
    }

    private RagResult parseItem(JsonObject item) {
        return new RagResult(
                item.get(REPO).getAsString(),
                item.get(TEXT).getAsString(),
                item.get(URL).getAsString(),
                item.get(SIMILARITY_SCORE).getAsDouble()
        );
    }
}
