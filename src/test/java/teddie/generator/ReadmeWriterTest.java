package teddie.generator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ReadmeWriterTest {
    @TempDir
    Path tempDir;

    @DisplayName("README.md 파일을 생성하고 미션 내용을 작성")
    @Test
    void README_md_파일을_생성하고_미션_내용을_작성한다() throws IOException {
        //given
        ReadmeWriter writer = new ReadmeWriter();
        String content = "# 로또 미션\n\n## 기능 요구사항";

        //when
        writer.createReadme(tempDir, content);

        //then
        Path readme = tempDir.resolve("README.md");
        assertThat(Files.exists(readme)).isTrue();
        assertThat(Files.readString(readme)).isEqualTo(content);
    }
}
