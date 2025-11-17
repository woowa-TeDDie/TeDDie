package teddie.controller;

import teddie.generator.PackageStructureBuilder;
import teddie.generator.ProjectWriter;
import teddie.generator.TemplateCopier;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectGeneratorController {
    private final TemplateCopier templateCopier;
    private final PackageStructureBuilder packageBuilder;
    private final ProjectWriter projectWriter;

    public ProjectGeneratorController() {
        this.templateCopier = new TemplateCopier();
        this.packageBuilder = new PackageStructureBuilder();
        this.projectWriter = new ProjectWriter();
    }

    public Path createProject(Path baseDir, String projectName, String packageName, String readmeContent) {
        Path projectPath = baseDir.resolve(projectName);
        try {
            Files.createDirectories(projectPath);
            templateCopier.copy(projectPath);
            packageBuilder.moveFilesToPackage(projectPath, packageName);
            projectWriter.writeProject(projectPath, projectName, packageName, readmeContent);
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 프로젝트 생성 실패: " + e.getMessage(), e);
        }
        return projectPath;
    }
}