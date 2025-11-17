package teddie.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SettingsGradleReplacer {
    private static final String SETTINGS_GRADLE = "settings.gradle";
    private static final String REPLACE_TARGET = "{{PROJECT_NAME}}";

    public void replaceGradleProjectName(Path projectPath, String projectName) throws IOException {
        Path settingGradle = projectPath.resolve(SETTINGS_GRADLE);
        String content = Files.readString(settingGradle);
        String replaced = content.replace(REPLACE_TARGET, projectName);
        Files.writeString(settingGradle, replaced);
    }
}
