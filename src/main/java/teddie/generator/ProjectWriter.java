package teddie.generator;

import java.io.IOException;
import java.nio.file.Path;

public class ProjectWriter {
    private final FileReplacer fileReplacer;
    private final ReadmeWriter readmeWriter;

    public ProjectWriter() {
        this.fileReplacer = new FileReplacer();
        this.readmeWriter = new ReadmeWriter();
    }

    public void writeProject(Path projectPath, String projectName, String packageName, String missionContent) throws IOException {
        fileReplacer.replaceProjectName(projectPath, projectName);
        fileReplacer.replacePackageState(projectPath, packageName);
        readmeWriter.createReadme(projectPath, missionContent);
    }
}
