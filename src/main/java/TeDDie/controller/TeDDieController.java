package TeDDie.controller;

import TeDDie.domain.CommandLineArgs;
import TeDDie.domain.Difficulty;
import TeDDie.domain.Topic;
import TeDDie.generator.ProjectGenerator;
import TeDDie.service.MissionService;
import TeDDie.view.OutputView;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TeDDieController {
    private static final String PROJECT_PREFIX = "java";
    private static final String USER_HOME_PROPERTY = "user.home";
    private static final String USER_DESKTOP = "Desktop";

    private final MissionService missionService;
    private final OutputView outputView;
    private final ProjectGenerator projectGenerator;

    public TeDDieController(MissionService missionService, OutputView outputView, ProjectGenerator projectGenerator) {
        this.missionService = missionService;
        this.outputView = outputView;
        this.projectGenerator = projectGenerator;
    }

    public void run(String args[]) {
        try {
            String missionResult = generateMission(args);
            generateProject(args);
            outputView.printMission(missionResult);
        } catch (Exception e) {
            outputView.printError(e.getMessage());
        }

    }

    private String generateMission(String[] args) throws Exception {
        CommandLineArgs commandLineArgs = new CommandLineArgs(args);
        Topic topic = new Topic(commandLineArgs.getTopic());
        Difficulty difficulty = Difficulty.from(commandLineArgs.getDifficulty());
        return missionService.generateMission(topic, difficulty);
    }

    private void generateProject(String[] args) {
        CommandLineArgs commandLineArgs = new CommandLineArgs(args);
        Topic topic = new Topic(commandLineArgs.getTopic());

        Path desktopPath = getDesktopPath();
        String projectName = PROJECT_PREFIX + topic.getValue();
        projectGenerator.createProject(desktopPath, projectName, topic.getValue(), projectName);
    }

    private Path getDesktopPath() {
        String userHome = System.getProperty(USER_HOME_PROPERTY);
        return Path.of(userHome, USER_DESKTOP);
    }
}
