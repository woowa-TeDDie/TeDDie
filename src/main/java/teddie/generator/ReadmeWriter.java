package teddie.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadmeWriter {
    private static final String README = "README.md";

    public void createReadme(Path projectPath, String missionContent) {
        try {
            writeReadme(projectPath, missionContent);
        } catch (IOException e) {
            throw new RuntimeException("README 파일 생성 실패: " + e.getMessage(), e);
        }
    }

    private void writeReadme(Path projectPath, String missionContent) throws IOException {
        Path readme = projectPath.resolve(README);
        Files.writeString(readme, missionContent);
    }
}