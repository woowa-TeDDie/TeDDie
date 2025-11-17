package teddie.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PackageStructureBuilder {
    private static final String MAIN_PACKAGE = "src/main/java";
    private static final String TEST_PACKAGE = "src/test/java";
    private static final String APPLICATION = "/Application.java";
    private static final String APPLICATION_TEST = "/ApplicationTest.java";
    private static final String PACKAGE_SEPERATOR = "/";

    public void moveFilesToPackage(Path projectPath, String packageName) {
        try {
            moveMainFiles(projectPath, packageName);
            moveTestFiles(projectPath, packageName);
        } catch (IOException e) {
            throw new RuntimeException("패키지 구조 생성 실패: " + e.getMessage(), e);
        }
    }

    private void moveMainFiles(Path projectPath, String packageName) throws IOException {
        Path mainPackage = createPackageDirectory(projectPath, MAIN_PACKAGE, packageName);
        moveFile(projectPath, MAIN_PACKAGE + APPLICATION, mainPackage);
    }

    private void moveTestFiles(Path projectPath, String packageName) throws IOException {
        Path testPackage = createPackageDirectory(projectPath, TEST_PACKAGE, packageName);
        moveFile(projectPath, TEST_PACKAGE + APPLICATION_TEST, testPackage);
    }

    private Path createPackageDirectory(Path projectPath, String basePath, String packageName) throws IOException {
        Path packagePath = projectPath.resolve(basePath + PACKAGE_SEPERATOR + packageName);
        Files.createDirectories(packagePath);
        return packagePath;
    }

    private void moveFile(Path projectPath, String sourcePath, Path targetPath) throws IOException {
        Path source = projectPath.resolve(sourcePath);
        Path fileName = source.getFileName();
        Path destination = targetPath.resolve(fileName);
        Files.move(source, destination);
    }
}
