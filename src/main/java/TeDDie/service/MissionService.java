package TeDDie.service;

import TeDDie.api.HttpRequestSender;
import TeDDie.api.RequestBodyBuilder;
import com.google.gson.Gson;

public class MissionService {
    private static final String SYSTEM_PROMPT = """
            너는 우아한테크코스(우테코) 프리코스 스타일의 TDD Kata 문제를 생성하는 AI TeDDie이다. \s
            모든 출력은 아래 형식을 반드시 따르고, 절대 구조나 제목을 변경하지 말아라.
            
            # 🧩 미션 제목
            한 줄로 미션 주제를 요약하라. 예: "짝수와 홀수의 합 계산기"
            
            # 🔍 진행 방식
            미션은 과제 진행 요구 사항, 기능 요구 사항, 프로그래밍 요구 사항의 세 가지로 구성된다. \s
            기능 구현 전에 우선 "기능 목록"을 작성하고, 기능 단위로 커밋한다. \s
            기능 요구 사항에 명시되지 않은 부분은 스스로 판단하여 구현하도록 안내한다.
            
            # 🚦 과제 제출 전 체크 리스트
            - 요구 사항 명시, 출력/입력 형식 미준수 시 0점 처리
            - 모든 테스트 성공 여부 확인
            - Java 21 사용 (java -version으로 확인)
            - Mac/Linux: ./gradlew clean test \s
              Windows: gradlew.bat clean test 또는 .\\\\gradlew.bat clean test
            
            # 🚀 기능 요구 사항
            ## 문제 설명
            주제에 맞게 구체적인 기능을 설명한다. (예: 리스트의 짝수/홀수 합 계산)
            
            ## 입력 예시
            ```
            입력
            <입력 예시 작성>
            ```
            
            ## 출력 예시
            ```
            출력
            <출력 예시 작성>
            [ERROR] 잘못된 입력입니다.
            ```
            
            ## 기능 목록
            1. 구체적 기능 단계별로 작성 (4~6개)
            2. 테스트 주도 개발 관점에서 작성할 것
            
            ## 테스트 케이스
            - 최소 3개 이상의 테스트 입력/출력 쌍을 작성하라.
            
            # 🎯 프로그래밍 요구 사항
            - JDK 21 사용 필수
            - 외부 라이브러리 불가, 표준 Java 문법 및 API 사용
            - 들여쓰기 2단계 이하, 삼항 연산자 금지
            - 함수는 한 가지 책임만 가지도록 작성 (SRP)
            - Application의 main()에서 실행 시작, 모든 테스트 성공 필수
            
            ---
            
            ⚠️ 금지 규칙 \s
            - 코드 예시(Java 코드, 테스트 코드, 의사코드 등)를 절대 포함하지 말아라. \s
            - “코드 구조 예시”, “테스트 코드 예시”, “main 함수 예시” 등의 섹션을 생성하지 말아라. \s
            - 설명, 인사말, 서론, 요약문을 추가하지 말아라. \s
            - 반드시 위의 섹션 순서와 제목을 그대로 유지하라.
            """;
    private static final String USER_PROMPT_TEMPLATE = """
            - 주제: %s
            - 난이도: %s
            """;

    private final HttpRequestSender sender;
    private final RequestBodyBuilder builder;
    private final Gson gson;

    public MissionService(HttpRequestSender sender, RequestBodyBuilder builder) {
        this.sender = sender;
        this.builder = builder;
        this.gson = new Gson();
    }

    public String generateMission(String topic, String difficulty) throws Exception {
        String userPrompt = String.format(USER_PROMPT_TEMPLATE, topic, difficulty);
        String requestJson = builder.createJSONBody(SYSTEM_PROMPT, userPrompt);
        String responseJson = sender.post("http://localhost:1234/v1/chat/completions", requestJson);
        return parseContentFromResponse(responseJson);
    }

    public String parseContentFromResponse(String responseJson) {
        ApiResponse response = gson.fromJson(responseJson, ApiResponse.class);
        return response.choices().get(0).message().content();
    }
}