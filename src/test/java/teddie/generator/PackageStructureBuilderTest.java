package teddie.generator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class PackageStructureBuilderTest {
    @TempDir
    Path tempDir;

    @DisplayName("패키지 디렉토리를 생성하고 Application.java/ApplicationTest.java를 이동")
    @Test
    void 패키지_디렉토리를_생성하고_Application_java_ApplicationTest_java를_이동() throws Exception {
        //given
        PackageStructureBuilder builder = new PackageStructureBuilder();
        Files.createDirectories(tempDir.resolve("src/main/java"));
        Files.createDirectories(tempDir.resolve("src/test/java"));
        Files.writeString(tempDir.resolve("src/main/java/Application.java"), "class App {}");
        Files.writeString(tempDir.resolve("src/test/java/ApplicationTest.java"), "class Test {}");

        //when
        builder.moveFilesToPackage(tempDir, "lotto");

        //then
        assertThat(Files.exists(tempDir.resolve("src/main/java/lotto/Application.java"))).isTrue();
        assertThat(Files.exists(tempDir.resolve("src/test/java/lotto/ApplicationTest.java"))).isTrue();
        assertThat(Files.exists(tempDir.resolve("src/main/java/Application.java"))).isFalse();
    }
}
