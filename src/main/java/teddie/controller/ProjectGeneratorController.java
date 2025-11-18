package teddie.controller;

import teddie.generator.PackageStructureBuilder;
import teddie.generator.ProjectWriter;
import teddie.generator.TemplateCopier;
import teddie.service.MissionResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectGeneratorController {
    private static final String USER_HOME_PROPERTY = "user.home";
    private static final String USER_DESKTOP = "Desktop";

    private final TemplateCopier templateCopier;
    private final PackageStructureBuilder packageBuilder;
    private final ProjectWriter projectWriter;

    public ProjectGeneratorController(TemplateCopier templateCopier, PackageStructureBuilder packageBuilder, ProjectWriter projectWriter) {
        this.templateCopier = templateCopier;
        this.packageBuilder = packageBuilder;
        this.projectWriter = projectWriter;
    }

    public Path createProject(String projectName, String packageName, MissionResponse missionResponse) {
        Path baseDir = getDesktopPath();
        Path projectPath = baseDir.resolve(projectName);
        try {
            Files.createDirectories(projectPath);
            templateCopier.copy(projectPath);
            packageBuilder.moveFilesToPackage(projectPath, packageName);
            projectWriter.writeProject(projectPath, projectName, packageName);
            projectWriter.writeREADME(projectPath, missionResponse.mission());
            projectWriter.writeTestFile(projectPath, packageName, missionResponse.testCases());
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 프로젝트 생성 실패: " + e.getMessage(), e);
        }
        return projectPath;
    }

    private Path getDesktopPath() {
        String userHome = System.getProperty(USER_HOME_PROPERTY);
        return Path.of(userHome, USER_DESKTOP);
    }
}
