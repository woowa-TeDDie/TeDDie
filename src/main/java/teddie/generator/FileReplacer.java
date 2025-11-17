package teddie.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReplacer {
    private static final String SETTINGS_GRADLE = "settings.gradle";
    private static final String REPLACE_TARGET = "{{PROJECT_NAME}}";
    private static final String APPLICATION = "/Application.java";
    private static final String APPLICATION_TEST = "/ApplicationTest.java";
    private static final String MAIN_PACKAGE = "src/main/java";
    private static final String TEST_PACKAGE = "src/test/java";
    private static final String PACKAGE_SEPERATOR = "/";
    private static final String PACKAGE_STATEMENT = "package ";
    private static final String PACKAGE_SUFFIX = ";\n\n";

    public void replaceProjectName(Path projectPath, String projectName) throws IOException {
        Path settingGradle = projectPath.resolve(SETTINGS_GRADLE);
        String content = Files.readString(settingGradle);
        String replaced = content.replace(REPLACE_TARGET, projectName);
        Files.writeString(settingGradle, replaced);
    }

    public void replacePackageState(Path projectPath, String packageName) throws IOException {
        replacePackageName(projectPath, MAIN_PACKAGE + PACKAGE_SEPERATOR + packageName + APPLICATION, packageName);
        replacePackageName(projectPath, TEST_PACKAGE + PACKAGE_SEPERATOR + packageName + APPLICATION_TEST, packageName);
    }

    private void replacePackageName(Path projectPath, String filePath, String packageName) throws IOException {
        Path targetPath = projectPath.resolve(filePath);
        String content = Files.readString(targetPath);
        String packageStatement = PACKAGE_STATEMENT + packageName + PACKAGE_SUFFIX;
        String newContent = packageStatement + content;
        Files.writeString(targetPath, newContent);
    }
}
