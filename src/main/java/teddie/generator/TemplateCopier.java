package teddie.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class TemplateCopier {
    private static final String TEMPLATE_PATH = "/template/";

    public void copy(Path targetPath) throws IOException {
        Path templatePath = getTemplatePath();
        try (Stream<Path> paths = Files.walk(templatePath)) {
            paths.forEach(source -> copyFile(source, templatePath, targetPath));
        }
    }

    private void copyFile(Path source, Path templatePath, Path targetPath) {
        Path destination = targetPath.resolve(templatePath.relativize(source));
        if (Files.isDirectory(source)) {
            createDirectories(destination);
            return;
        }
        copyRegularFile(source, targetPath);
    }

    private Path getTemplatePath() {
        try {
            return Path.of(getClass().getResource(TEMPLATE_PATH).toURI());
        } catch (Exception e) {
            throw new RuntimeException("[ERROR] 템플릿 경로를 찾을 수 없습니다: " + e.getMessage(), e);
        }
    }

    private void createDirectories(Path destination) {
        try {
            Files.createDirectories(destination);
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 디렉토리가 생성되지 않았습니다.");
        }
    }

    private void copyRegularFile(Path source, Path targetPath) {
        try {
            Files.copy(source, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 디렉토리가 생성되지 않았습니다.");
        }
    }
}
