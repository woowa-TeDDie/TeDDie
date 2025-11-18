package teddie.generator;

import teddie.service.TestCase;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TestGenerator {
    private static final String TEST_TEMPLATE = """
            package %s;
            
            import camp.nextstep.edu.missionutils.test.NsTest;
            import org.junit.jupiter.api.Test;
            
            import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
            import static org.assertj.core.api.Assertions.assertThat;
            import static org.assertj.core.api.Assertions.assertThatThrownBy;
            
            class ApplicationTest extends NsTest {
                private static final String ERROR_MESSAGE = "[ERROR]";
            
            %s
            
                @Override
                public void runMain() {
                    Application.main(new String[]{});
                }
            }
            """;

    public void generateTestFile(Path projectPath, String packageName, List<TestCase> testCases) {
        try {
            String testContent = generateTestCode(packageName, testCases);
            Path testFilePath = projectPath.resolve(
                    "src/test/java/" + packageName + "/ApplicationTest.java"
            );
            Files.writeString(testFilePath, testContent);
        } catch (IOException e) {
            throw new RuntimeException("테스트 파일 생성 실패: " + e.getMessage(), e);
        }
    }

    private String generateTestCode(String packageName, List<TestCase> testCases) {
        if (testCases.isEmpty()) {
            return String.format(TEST_TEMPLATE, packageName, generateDefaultTests());
        }
        StringBuilder methods = new StringBuilder();
        for (TestCase testCase : testCases) {
            methods.append(generateTestMethod(testCase));
            methods.append("\n");
        }
        return String.format(TEST_TEMPLATE, packageName, methods.toString());
    }

    private String generateTestMethod(TestCase testCase) {
        if (testCase.expectError()) {
            return generateExceptionTest(testCase);
        }
        return generateFunctionalTest(testCase);
    }

    private String generateFunctionalTest(TestCase testCase) {
        return """
                    @Test
                    void %s() {
                        assertSimpleTest(() -> {
                            run("%s");
                            assertThat(output()).contains("%s");
                        });
                    }
                """.formatted(
                testCase.name(),
                escapeString(testCase.input()),
                escapeString(testCase.expectedOutput())
        );
    }

    private String generateExceptionTest(TestCase testCase) {
        return """
                    @Test
                    void %s() {
                        assertSimpleTest(() ->
                            assertThatThrownBy(() -> runException("%s"))
                                .isInstanceOf(IllegalArgumentException.class)
                        );
                    }
                """.formatted(
                testCase.name(),
                escapeString(testCase.input())
        );
    }

    private String generateDefaultTests() {
        return """
                    @Test
                    void 기능_테스트() {
                        assertSimpleTest(() -> {
                            run("");
                            assertThat(output()).isNotEmpty();
                        });
                    }

                    @Test
                    void 예외_테스트() {
                        assertSimpleTest(() ->
                            assertThatThrownBy(() -> runException(""))
                                .isInstanceOf(IllegalArgumentException.class)
                        );
                    }
                """;
    }

    private String escapeString(String str) {
        return str.replace("\"", "\\\"");
    }
}
