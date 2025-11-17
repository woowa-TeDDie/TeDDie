package teddie.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadmeWriter {
    private static final String README = "README.md";

    public void createReadme(Path projectPath, String missionContent) throws IOException {
        Path readme = projectPath.resolve(README);
        Files.writeString(readme, missionContent);
    }
}
