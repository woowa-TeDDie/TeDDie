package teddie.generator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TemplateCopierTest {
    @TempDir
    Path tempDir;

    @DisplayName("템플릿을 복사하여 프로젝트 생성")
    @Test
    void 템플릿을_복사하여_프로젝트_생성() throws IOException {
        //given
        String projectName = "java-lotto";

        // when
        TemplateCopier copier = new TemplateCopier();
        Path projectPath = tempDir.resolve(projectName);

        Files.createDirectories(projectPath);
        copier.copy(projectPath);

        // then
        assertThat(Files.exists(projectPath)).isTrue();
        assertThat(Files.exists(projectPath.resolve("build.gradle"))).isTrue();
        assertThat(Files.exists(projectPath.resolve("settings.gradle"))).isTrue();
        assertThat(Files.exists(projectPath.resolve("src/main/java"))).isTrue();
        assertThat(Files.exists(projectPath.resolve("src/test/java"))).isTrue();
    }
}
