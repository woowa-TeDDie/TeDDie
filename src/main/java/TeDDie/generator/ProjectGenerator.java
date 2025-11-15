package TeDDie.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class ProjectGenerator {
    private static final String TEMPLATE_PATH = "/template";

    public Path createProject(Path baseDir, String projectName, String packageName) {
        Path projectPath = baseDir.resolve(projectName);
        try {
            Files.createDirectories(projectPath);
            copyTemplate(projectPath);
            replaceProjectName(projectPath, projectName);
            moveFilesToPackage(projectPath, packageName);
            replacePackageState(projectPath, packageName);
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

    private void replaceProjectName(Path projectrPath, String projectName) throws IOException {
        Path settingsGradle = projectrPath.resolve("settings.gradle");
        String content = Files.readString(settingsGradle);
        String replaced = content.replace("{{PROJECT_NAME}}", projectName);
        Files.writeString(settingsGradle, replaced);
    }

    private void moveFilesToPackage(Path projectPath, String packageName) throws IOException {
        moveMainFiles(projectPath, packageName);
        moveTestFiles(projectPath, packageName);
    }

    private void moveMainFiles(Path projectPath, String packageName) throws IOException {
        Path mainPackage = createPackageDirectory(projectPath, "src/main/java", packageName);
        moveFile(projectPath, "src/main/java/Application.java", mainPackage);
    }

    private void moveTestFiles(Path projectPath, String packageName) throws IOException {
        Path testPackage = createPackageDirectory(projectPath, "src/test/java", packageName);
        moveFile(projectPath, "src/test/java/ApplicationTest.java", testPackage);
    }

    private Path createPackageDirectory(Path projectPath, String basePath, String packageName) throws IOException {
        Path packagePath = projectPath.resolve(basePath + "/" + packageName);
        Files.createDirectories(packagePath);
        return packagePath;
    }

    private void moveFile(Path projectPath, String sourcePath, Path targetPath) throws IOException {
        Path source = projectPath.resolve(sourcePath);
        Path fileName = source.getFileName();
        Path destination = targetPath.resolve(fileName);
        Files.move(source, destination);
    }

    private void replacePackageState(Path projectPath, String packageName) throws IOException {
        replacePackageName(projectPath, "src/main/java/" + packageName + "/Application.java", packageName);
        replacePackageName(projectPath, "src/test/java/" + packageName + "/ApplicationTest.java", packageName);
    }

    private void replacePackageName(Path projectPath, String filePath, String packageName) throws IOException {
        Path targetPath = projectPath.resolve(filePath);
        String content = Files.readString(targetPath);
        String packageStatement = "package " + packageName + ";\n\n";  // \n\n 추가!
        String newContent = packageStatement + content;
        Files.writeString(targetPath, newContent);
    }
}
