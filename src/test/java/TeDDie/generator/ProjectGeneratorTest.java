package TeDDie.generator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ProjectGeneratorTest {
    @TempDir
    Path tempDir;

    private ProjectGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new ProjectGenerator();
    }

    @DisplayName("템플릿을 복사하여 프로젝트 생성")
    @Test
    void 템플릿을_복사하여_프로젝트_생성() {
        //given
        String projectName = "java-lotto";

        //when
        Path projectPath = generator.createProject(tempDir, projectName);

        //then
        assertThat(Files.exists(projectPath)).isTrue();
        assertThat(Files.exists(projectPath.resolve("build.gradle"))).isTrue();
        assertThat(Files.exists(projectPath.resolve("settings.gradle"))).isTrue();
        assertThat(Files.exists(projectPath.resolve("src/main/java"))).isTrue();
        assertThat(Files.exists(projectPath.resolve("src/test/java"))).isTrue();
    }
}
