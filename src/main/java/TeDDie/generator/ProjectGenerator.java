package TeDDie.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class ProjectGenerator {
    private static final String TEMPLATE_PATH = "/template";

    public Path createProject(Path baseDir, String projectName) {
        Path projectPath = baseDir.resolve(projectName);
        try {
            Files.createDirectories(projectPath);
            copyTemplate(projectPath);
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 프로젝트 생성 실패: " + e.getMessage(), e);
        }
        return projectPath;
    }

    private void copyTemplate(Path targetPath) throws IOException {
        Path templatePath = getTemplatePath();
        try (Stream<Path> paths = Files.walk(templatePath)) {
            paths.forEach(source -> copyFile(source, templatePath, targetPath));
        }
    }

    private void copyFile(Path source, Path templatePath, Path targetPath) {
        try {
            Path destination = targetPath.resolve(templatePath.relativize(source));
            if (Files.isDirectory(source)) {
                Files.createDirectories(destination);
                return;
            }
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getTemplatePath() {
        try {
            return Path.of(getClass().getResource(TEMPLATE_PATH).toURI());
        } catch (Exception e) {
            throw new RuntimeException("[ERROR] 템플릿 경로를 찾을 수 없습니다: " + e.getMessage(), e);
        }
    }
}
