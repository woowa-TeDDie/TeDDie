package teddie.service;

import static teddie.prompt.SystemPrompt.SYSTEM_PROMPT;

import teddie.common.util.HttpRequestSender;
import teddie.api.RagClient;
import teddie.api.dto.RagResult;
import teddie.domain.Difficulty;
import teddie.domain.Topic;
import teddie.prompt.UserPrompt;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MissionService {
    private final HttpRequestSender sender;
    private final RequestBodyBuilder builder;
    private final RagClient ragClient;

    public MissionService(HttpRequestSender sender, RequestBodyBuilder builder, RagClient ragClient) {
        this.sender = sender;
        this.builder = builder;
        this.ragClient = ragClient;
    }

    public MissionResponse generateMission(Topic topic, Difficulty difficulty) {
        List<RagResult> ragResults = ragClient.search(topic.getValue(), 3);
        UserPrompt userPrompt = new UserPrompt(topic, difficulty, ragResults);
        String requestJson = builder.createJSONBody(SYSTEM_PROMPT, userPrompt.getContent());
        String responseJson = sender.post("http://localhost:1234/v1/chat/completions", requestJson);
        String content = parseContentFromResponse(responseJson);

        return parseToMissionResponse(content);
    }

    private String parseContentFromResponse(String responseJson) {
        Gson gson = new Gson();
        ApiResponse response = gson.fromJson(responseJson, ApiResponse.class);
        return response.extractResponse();
    }

    private MissionResponse parseToMissionResponse(String content) {
        if (!content.contains("## 테스트 케이스")) {
            return new MissionResponse(content, List.of());
        }
        String[] parts = content.split("## 테스트 케이스", 2);
        String mission = parts[0].strip();
        String testSection = parts[1].strip();
        List<TestCase> testCases = extractTestCases(testSection);
        return new MissionResponse(mission, testCases);
    }

    private List<TestCase> extractTestCases(String testSection) {
        List<TestCase> testCases = new ArrayList<>();
        TestCase functionalTest = extractFunctionalTest(testSection);
        if (functionalTest != null) {
            testCases.add(functionalTest);
        }
        TestCase exceptionTest = extractExceptionTest(testSection);
        if (exceptionTest != null) {
            testCases.add(exceptionTest);
        }
        return testCases;
    }

    private TestCase extractFunctionalTest(String testSection) {
        Pattern pattern = Pattern.compile(
                "### 기능 테스트\\s*-\\s*입력:\\s*(.+?)\\s*-\\s*출력:\\s*(.+?)(?=##|$)",
                Pattern.DOTALL
        );
        Matcher matcher = pattern.matcher(testSection);
        if (matcher.find()) {
            String input = matcher.group(1).trim();
            String output = matcher.group(2).trim();
            return new TestCase("기능_테스트", "기능 테스트", input, output, false);
        }
        return null;
    }

    private TestCase extractExceptionTest(String testSection) {
        Pattern pattern = Pattern.compile(
                "### 예외 테스트\\s*-\\s*입력:\\s*(.+?)(?=##|$)",
                Pattern.DOTALL
        );
        Matcher matcher = pattern.matcher(testSection);
        if (matcher.find()) {
            String input = matcher.group(1).trim();
            return new TestCase("예외_테스트", "예외 테스트", input, "", true);
        }
        return null;
    }
}
