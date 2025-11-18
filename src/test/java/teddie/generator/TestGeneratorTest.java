package teddie.generator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import teddie.service.TestCase;

public class TestGeneratorTest {
    @TempDir
    Path tempDir;

    @DisplayName("기능 테스트와 예외 테스트가 포함된 ApplicationTest 생성")
    @Test
    void 기능_테스트와_예외_테스트가_포함된_ApplicationTest_생성() throws Exception {
        //given
        TestGenerator testGenerator = new TestGenerator();
        String packageName = "racingcar";
        Path testDir = tempDir.resolve("src/test/java/racingcar");
        Files.createDirectories(testDir);
        List<TestCase> testCases = List.of(
                new TestCase("기능_테스트", "기능 테스트", "pobi,woni\n1", "pobi : -", false),
                new TestCase("예외_테스트", "예외 테스트", "pobi,javaji\n1", "", true)
        );

        //when
        testGenerator.generateTestFile(tempDir, packageName, testCases);

        //then
        Path testFile = testDir.resolve("ApplicationTest.java");

        String content = Files.readString(testFile);
        assertThat(content).contains("package racingcar;");
        assertThat(content).contains("class ApplicationTest extends NsTest");
        assertThat(content).contains("@Test");
        assertThat(content).contains("void 기능_테스트()");
        assertThat(content).contains("void 예외_테스트()");
        assertThat(content).contains("run(\"pobi,woni");
        assertThat(content).contains("1\")");
        assertThat(content).contains("runException(\"pobi,javaji");
    }
}
