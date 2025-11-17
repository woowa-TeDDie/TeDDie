package teddie.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PackageStatementReplacer {
    private static final String APPLICATION = "/Application.java";
    private static final String APPLICATION_TEST = "/ApplicationTest.java";
    private static final String MAIN_PACKAGE = "src/main/java";
    private static final String TEST_PACKAGE = "src/test/java";
    private static final String PACKAGE_SEPERATOR = "/";
    private static final String PACKAGE_STATEMENT = "package ";
    private static final String PACKAGE_SUFFIX = ";\n\n";

    public void replacePackageState(Path projectPath, String packageName) {
        try {
            replacePackageName(projectPath, MAIN_PACKAGE + PACKAGE_SEPERATOR + packageName + APPLICATION, packageName);
            replacePackageName(projectPath, TEST_PACKAGE + PACKAGE_SEPERATOR + packageName + APPLICATION_TEST, packageName);
        } catch (IOException e) {
            throw new RuntimeException("패키지 구문 변경 실패: " + e.getMessage(), e);
        }
    }

    private void replacePackageName(Path projectPath, String filePath, String packageName) throws IOException {
        Path targetPath = projectPath.resolve(filePath);
        String content = Files.readString(targetPath);
        String packageStatement = PACKAGE_STATEMENT + packageName + PACKAGE_SUFFIX;
        String newContent = packageStatement + content;
        Files.writeString(targetPath, newContent);
    }
}
