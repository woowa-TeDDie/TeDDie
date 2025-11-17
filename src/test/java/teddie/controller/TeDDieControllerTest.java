package teddie.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import teddie.domain.Difficulty;
import teddie.domain.Topic;
import teddie.service.MissionService;
import teddie.view.OutputView;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TeDDieControllerTest {
    @Mock
    private MissionService mockService;

    @Mock
    private OutputView mockView;

    @Mock
    private ProjectGeneratorController mockProjectGeneratorController;

    @InjectMocks
    private TeDDieController controller;

    @DisplayName("CLI 인자를 파싱하여 Service와 View를 올바르게 호출")
    @Test
    void CLI_인자를_파싱하여_Service와_View를_올바르게_호출() {
        //given
        String[] args = {"--topic", "collection", "--difficulty", "easy"};
        String missionResult = "## 미션";
        when(mockService.generateMission(any(Topic.class), any(Difficulty.class)))
                .thenReturn(missionResult);

        //when
        controller.run(args);

        //then
        verify(mockService).generateMission(any(Topic.class), any(Difficulty.class));
        verify(mockView).printMission(missionResult);
    }

    @DisplayName("Service에서 예외 발생 시 View의 printError를_호출")
    @Test
    void Service에서_예외_발생_시_View의_printError를_호출() {
        //given
        String[] args = {"--topic", "collection", "--difficulty", "easy"};
        String errorMessage = "API 요청 실패";
        when(mockService.generateMission(any(Topic.class), any(Difficulty.class)))
                .thenThrow(new RuntimeException(errorMessage));

        //when
        controller.run(args);

        //then
        verify(mockView).printError("[ERROR] 알 수 없는 오류 - API 요청 실패");
    }

    @DisplayName("미션 생성 후 바탕화면에 프로젝트 생성")
    @Test
    void 미션_생성_후_바탕화면에_프로젝트_생성() {
        //given
        String[] args = {"--topic", "collection", "--difficulty", "easy"};
        String missionResult = "java-collection";
        when(mockService.generateMission(any(Topic.class), any(Difficulty.class)))
                .thenReturn(missionResult);

        //when
        controller.run(args);

        //then
        verify(mockProjectGeneratorController).createProject(
                anyString(),
                eq("collection"),
                eq(missionResult)
        );

    }
}