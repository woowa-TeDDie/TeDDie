package teddie.generator;

import java.nio.file.Path;

public class ProjectWriter {
    private final PackageStatementReplacer packageStatementReplacer;
    private final ReadmeWriter readmeWriter;
    private final SettingsGradleReplacer settingsGradleReplacer;

    public ProjectWriter(PackageStatementReplacer packageStatementReplacer, ReadmeWriter readmeWriter, SettingsGradleReplacer settingsGradleReplacer) {
        this.packageStatementReplacer = packageStatementReplacer;
        this.readmeWriter = readmeWriter;
        this.settingsGradleReplacer = settingsGradleReplacer;
    }

    public void writeProject(Path projectPath, String projectName, String packageName) {
        settingsGradleReplacer.replaceGradleProjectName(projectPath, projectName);
        packageStatementReplacer.replacePackageState(projectPath, packageName);
    }

    public void writeREADME(Path projectPath, String missionContent) {
        readmeWriter.createReadme(projectPath, missionContent);
    }
}
