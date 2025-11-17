package teddie.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import teddie.generator.PackageStructureBuilder;
import teddie.generator.ProjectWriter;
import teddie.generator.TemplateCopier;

@ExtendWith(MockitoExtension.class)
public class ProjectGeneratorControllerTest {

    @Mock
    private TemplateCopier mockTemplateCopier;

    @Mock
    private PackageStructureBuilder mockPackageBuilder;

    @Mock
    private ProjectWriter mockProjectWriter;

    @InjectMocks
    private ProjectGeneratorController generator;

    @DisplayName("프로젝트 생성 시 의존 객체들을 올바르게 호출한다")
    @Test
    void createProject_should_call_dependencies_correctly() throws IOException {
        // given
        String projectName = "test-project";
        String packageName = "testpackage";
        String readmeContent = "test readme";

        // when
        generator.createProject(projectName, packageName, readmeContent);

        // then
        verify(mockTemplateCopier).copy(any(Path.class));
        verify(mockPackageBuilder).moveFilesToPackage(any(Path.class), eq(packageName));
        verify(mockProjectWriter).writeProject(any(Path.class), eq(projectName), eq(packageName));
        verify(mockProjectWriter).writeREADME(any(Path.class), eq(readmeContent));
    }
}
