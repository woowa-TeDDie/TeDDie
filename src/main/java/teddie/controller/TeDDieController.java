package teddie.controller;

import teddie.domain.CommandLineArgs;
import teddie.domain.Difficulty;
import teddie.domain.Topic;
import teddie.service.MissionService;
import teddie.view.OutputView;
import java.nio.file.Path;

public class TeDDieController {
    private static final String PROJECT_PREFIX = "java";
    private static final String USER_HOME_PROPERTY = "user.home";
    private static final String USER_DESKTOP = "Desktop";

    private final MissionService missionService;
    private final OutputView outputView;
    private final ProjectGeneratorController projectGeneratorController;

    public TeDDieController(MissionService missionService, OutputView outputView, ProjectGeneratorController projectGeneratorController) {
        this.missionService = missionService;
        this.outputView = outputView;
        this.projectGeneratorController = projectGeneratorController;
    }

    public void run(String args[]) {
        try {
            CommandLineArgs commandLineArgs = new CommandLineArgs(args);

            String missionResult = generateMission(commandLineArgs);
            generateProject(commandLineArgs);
            outputView.printMission(missionResult);
        } catch (Exception e) {
            outputView.printError(e.getMessage());
        }

    }

    private String generateMission(CommandLineArgs commandLineArgs) throws Exception {
        Topic topic = new Topic(commandLineArgs.getTopic());
        Difficulty difficulty = Difficulty.from(commandLineArgs.getDifficulty());
        return missionService.generateMission(topic, difficulty);
    }

    private void generateProject(CommandLineArgs commandLineArgs) {
        Topic topic = new Topic(commandLineArgs.getTopic());

        Path desktopPath = getDesktopPath();
        String projectName = PROJECT_PREFIX + topic.getValue();
        projectGeneratorController.createProject(desktopPath, projectName, topic.getValue(), projectName);
    }

    private Path getDesktopPath() {
        String userHome = System.getProperty(USER_HOME_PROPERTY);
        return Path.of(userHome, USER_DESKTOP);
    }
}
