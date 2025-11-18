package teddie.generator;

import java.nio.file.Path;

public class ProjectReplacer {
    private final PackageStatementReplacer packageStatementReplacer;
    private final SettingsGradleReplacer settingsGradleReplacer;

    public ProjectReplacer(
            PackageStatementReplacer packageStatementReplacer,
            SettingsGradleReplacer settingsGradleReplacer
    ) {
        this.packageStatementReplacer = packageStatementReplacer;
        this.settingsGradleReplacer = settingsGradleReplacer;
    }

    public void replaceAll(Path projectPath, String projectName, String packageName) {
        settingsGradleReplacer.replaceGradleProjectName(projectPath, projectName);
        packageStatementReplacer.replacePackageState(projectPath, packageName);
    }
}