# 🧸 teddie - Java Application

> **우테코 프리코스 스타일 TDD 연습 문제 생성기**

---

## 🤔 왜 이 프로젝트를 만들었나요?

우아한테크코스 프리코스를 진행하면서 TDD의 가치를 체감했습니다.  
하지만 우테코 스타일의 연습 문제가 더 필요했고, "그럼 AI로 만들어보자!"라고 생각했습니다.

**핵심 아이디어:**
- 실제 우테코 프리코스 과제를 RAG로 학습
- 비슷한 스타일의 새로운 문제를 무한 생성
- 바로 풀 수 있도록 프로젝트 템플릿까지 자동 생성

---

## 📋 프로젝트 개요

### 주요 역할
- [X] CLI를 통한 사용자 입력 처리 (`--topic`, `--difficulty`)
- [X] 로컬 LLM API 호출 (LM Studio)
- [X] RAG API 연동 (우테코 과제 검색)
- [X] 프로젝트 템플릿 자동 생성
- [ ] 테스트 스켈레톤 생성 (계획 중)

---

## 🏗 시스템 아키텍처

### 하이브리드 구조를 선택한 이유

**초기 고민:**
- Java만으로 RAG를 구현? → FAISS, sentence-transformers가 Python 생태계에 더 성숙
- Python만 사용? → 우테코가 Java 중심이고, TDD 연습도 Java로 하고 싶음

**결정:**
- **Java**: 메인 애플리케이션, TDD 연습, 도메인 로직
- **Python**: RAG 시스템 (벡터 검색, 임베딩)
- **FastAPI**: Java ↔ Python 브릿지

```
┌──────────────────────────────────────────────┐
│      teddie Application (Java 21 + TDD)     │
│                                              │
│  ┌──────────────┐      ┌──────────────────┐ │
│  │ Controller   │ ───> │ MissionService   │ │
│  │  (CLI 처리)   │      │   (핵심 로직)     │ │
│  └──────────────┘      └──────────────────┘ │
│         │                      │             │
│         │                      ├─> RagClient
│         │                      ├─> HttpRequestSender
│         │                      └─> RequestBodyBuilder
│         │                                    │
│         └─> OutputView                      │
│                                              │
└──────────────────────────────────────────────┘
         │                                     │
         ▼                                     ▼
┌──────────────────┐               ┌──────────────────┐
│   LM Studio      │               │   RAG API        │
│  (로컬 LLM)       │               │  (FastAPI)       │
│   :1234          │               │   :8000          │
└──────────────────┘               └──────────────────┘
```

### 주요 설계 결정

#### 1. HTTP/1.1 명시 (HttpRequestSender)
**문제:** LM Studio가 HTTP/2를 완벽 지원하지 않아 연결 실패  
**해결:** `.version(HttpClient.Version.HTTP_1_1)` 명시

#### 2. RAG 검색 결과를 프롬프트에 포함
**고민:** LLM만으로도 되지 않나?  
**결과:** RAG 없이는 우테코 스타일 재현이 어려웠음 → RAG 필수

#### 3. Record 타입 적극 활용
**이유:** 불변 객체로 DTO 관리 + getter/setter 없이 구현

---

## 🤔 개발하면서 고민했던 점들

---

### 1. 테스트 전략 - Mock의 딜레마

**문제:**
- LM Studio API 테스트를 어떻게 하지?
- 실제 API 호출하면 테스트가 느리고 불안정함

**시도 1: MockWebServer 사용**
```java
@Test
void 서버가_200_OK_응답시_응답_본문을_그대로_반환() {
    mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(expectedBody));
    
    String result = sender.post(mockUrl, dummyRequestBody);
    
    assertThat(result).isEqualTo(expectedBody);
}
```

**시도 2: Mockito로 의존성 주입**
```java
@Mock
private HttpRequestSender mockSender;

@Test
void API_응답을_파싱하여_실제_텍스트_반환() {
    when(mockSender.post(anyString(), anyString()))
            .thenReturn(testResponse);
    // ...
}
```

**배운 점:**
- MockWebServer: HTTP 통신 자체를 테스트
- Mockito: 비즈니스 로직에 집중
- 상황에 따라 적절한 도구 선택이 중요

---

## 🚧 현재 진행 상황

### ✅ 완료

- [X] LLM API 통신 (HttpRequestSender)
- [X] JSON 요청/응답 처리 (Gson, RequestBodyBuilder)
- [X] RAG API 연동 (RagClient)
- [X] 미션 생성 로직 (MissionService)
- [X] CLI 인터페이스 (TeDDieController)
- [X] 콘솔 출력 (ConsoleView)
- [X] 모든 핵심 로직 단위 테스트 작성
- [X] MockWebServer를 활용한 통합 테스트
- [X] 프로젝트 템플릿 자동 생성
- [X] Markdown 파일 저장
- [X] 테스트 스켈레톤 생성

### 🔨 리팩토링 중

**우선순위 1: 디미터의 법칙 준수**
- [X] ApiResponse에 extractContent() 메서드 추가
- [X] 체이닝 호출 제거

**우선순위 2: 원시값 포장**
- [X] Topic 값 객체 생성
- [X] Difficulty Enum 생성
- [ ] Prompt 값 객체 생성

**우선순위 3: 일급 컬렉션**
- [ ] RagResults 일급 컬렉션 생성
- [ ] formatAsContext() 메서드로 포맷팅 로직 이동

**우선순위 4: 책임 분리**
- [ ] PromptGenerator 클래스 분리
- [ ] ResponseParser 클래스 분리
- [ ] MissionService 책임 경량화

**우선순위 5: 들여쓰기 개선**
- [ ] TeDDieController.parseArgs() 메서드 분리

---

## ✅ 우테코 코딩 컨벤션 체크리스트

### 현재 상태

- [x] 한 메서드에 오직 한 단계의 들여쓰기만
- [x] else 예약어 사용하지 않기
- [ ]️ 모든 원시값과 문자열 포장 (리팩토링 예정)
- [ ]️ 콜렉션에 대해 일급 컬렉션 적용 (리팩토링 예정)
- [x] 3개 이상의 인스턴스 변수를 가진 클래스 없음
- [x] getter/setter 없이 구현 (record 활용)
- [x] 메서드의 인자 수를 제한 (3개 이하)
- [ ]️ 코드 한 줄에 점(.) 하나만 (리팩토링 예정)
- [x] 메서드가 한 가지 일만 담당
- [x] 클래스를 작게 유지

---

## ⚠️ 예외 처리

| 분류 | 예외 상황 | 예시 입력 | 에러 메시지 | 발생 위치 |
|------|----------|----------|------------|----------|
| **CLI 인자** | 필수 인자 누락 | `--topic collection` (difficulty 없음) | `[ERROR] 필수 옵션을 입력해야 합니다.` | `TeDDieController` |
| | topic 공백 | `--topic "" --difficulty easy` | `[ERROR] 주제는 비어있을 수 없습니다.` | `Topic` (예정) |
| | 잘못된 난이도 | `--difficulty invalid` | `[ERROR] 유효하지 않은 난이도입니다.` | `Difficulty` (예정) |
| **API 통신** | LLM 서버 미응답 | (서버 중지 상태) | `ConnectException` | `HttpRequestSender` |
| | HTTP 에러 | (500 서버 에러) | `[ERROR] HTTP Error Status: 500` | `HttpRequestSender` |
| | RAG API 미응답 | (RAG 서버 중지) | `ConnectException` | `RagClient` |
| **응답 파싱** | choices 없음 | `{"choices": []}` | `[ERROR] API 응답에 choices가 없습니다.` | `ApiResponse` (예정) |
| | message null | `{"choices": [{"message": null}]}` | `[ERROR] API 응답에 message가 없습니다.` | `Choice` (예정) |
| | content 빈 문자열 | `{"choices": [{"message": {"content": ""}}]}` | `[ERROR] API 응답에 content가 없습니다.` | `Message` (예정) |

---

## 🧪 테스트 전략

### TDD 사이클

```
1. Red   → 실패하는 테스트 작성
2. Green → 최소한으로 구현
3. Refactor → 리팩토링
```

### 테스트 구조

**1. 단위 테스트 (Unit Test)**
- 각 클래스의 메서드별 테스트
- Mock 객체로 의존성 분리
- 예외 상황 테스트 포함

```java
@Test
@DisplayName("서버가 200 OK 응답시 응답 본문을 그대로 반환")
void 서버가_200_OK_응답시_응답_본문을_그대로_반환() {
    // given
    String expectedBody = "{\"response\":\"성공\"}";
    mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(expectedBody));
    
    // when
    String result = sender.post(mockUrl, dummyRequestBody);
    
    // then
    assertThat(result).isEqualTo(expectedBody);
}
```

**2. 통합 테스트 (Integration Test)**
- Controller - Service - View 연동
- Mock을 활용한 전체 흐름 검증

```java
@Test
void CLI_인자를_파싱하여_Service와_View를_올바르게_호출() {
    // given
    String[] args = {"--topic", "collection", "--difficulty", "easy"};
    when(mockService.generateMission("collection", "easy"))
            .thenReturn(missionResult);
    
    // when
    controller.run(args);
    
    // then
    verify(mockService).generateMission("collection", "easy");
    verify(mockView).printMission(missionResult);
}
```

**3. MockWebServer 활용**
- 실제 HTTP 통신 시뮬레이션
- 네트워크 레이어 테스트

### 테스트 커버리지 목표

- **핵심 비즈니스 로직**: 90% 이상
- **전체 코드**: 70% 이상
- **예외 처리 케이스**: 100%

---

## 🚀 빌드 및 실행

### 1. 의존성 설치

```bash
./gradlew build
```

### 2. 테스트 실행

```bash
./gradlew test
```

### 3. 애플리케이션 실행

```bash
./gradlew run --args="--topic collection --difficulty easy"
```

#### 실행 옵션

| 옵션 | 설명 | 필수 | 예시 |
|------|------|------|------|
| `--topic` | 생성할 문제의 주제 | ✅ | `collection`, `string`, `loop` |
| `--difficulty` | 난이도 | ✅ | `easy`, `medium`, `hard` |

---

## 👨‍💻 개발자

정용태 ([@jyt6640](https://github.com/jyt6640))

---

## 🔗 관련 리포지토리

- **teddie-RagSystem**: Python RAG 라이브러리
- **teddie-RagAPI**: FastAPI 검색 서버