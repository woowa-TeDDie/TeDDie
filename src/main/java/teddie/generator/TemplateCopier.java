package teddie.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class TemplateCopier {
    private static final String TEMPLATE_PATH = "/template/";

    public void copy(Path targetPath) {
        try {
            Path templatePath = getTemplatePath();
            copyTemplate(templatePath, targetPath);
        } catch (Exception e) {
            throw new RuntimeException("템플릿 복사 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private void copyTemplate(Path templatePath, Path targetPath) throws IOException {
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
            throw new RuntimeException("파일 처리 실패: " + e.getMessage(), e);
        }
    }

    private Path getTemplatePath() {
        try {
            return Path.of(getClass().getResource(TEMPLATE_PATH).toURI());
        } catch (Exception e) {
            throw new RuntimeException("템플릿 경로를 찾을 수 없습니다: " + e.getMessage(), e);
        }
    }
}