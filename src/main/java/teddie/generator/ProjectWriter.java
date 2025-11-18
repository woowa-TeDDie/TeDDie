package teddie.generator;

import java.nio.file.Path;
import java.util.List;
import teddie.service.TestCase;

public class ProjectWriter {
    private final ProjectReplacer projectReplacer;
    private final ReadmeWriter readmeWriter;
    private final TestGenerator testGenerator;


    public ProjectWriter(ReadmeWriter readmeWriter, ProjectReplacer projectReplacer, TestGenerator testGenerator) {
        this.readmeWriter = readmeWriter;
        this.projectReplacer = projectReplacer;
        this.testGenerator = testGenerator;
    }

    public void writeProject(Path projectPath, String projectName, String packageName) {
        projectReplacer.replaceAll(projectPath, projectName, packageName);
    }

    public void writeREADME(Path projectPath, String missionContent) {
        readmeWriter.createReadme(projectPath, missionContent);
    }

    public void writeTestFile(Path projectPath, String packageName, List<TestCase> testCases) {
        testGenerator.generateTestFile(projectPath, packageName, testCases);
    }
}
