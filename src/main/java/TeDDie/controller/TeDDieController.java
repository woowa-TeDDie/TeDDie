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
            CommandLineArgs commandLineArgs = new CommandLineArgs(args);
            Topic topic = new Topic(commandLineArgs.getTopic());
            Difficulty difficulty = Difficulty.from(commandLineArgs.getDifficulty());

            String missionResult = missionService.generateMission(topic, difficulty);

            Path desktopPath = getDesktopPath();
            String projectName = "java-" + topic.getValue();
            projectGenerator.createProject(desktopPath, projectName, topic.getValue(), missionResult);

            outputView.printMission(missionResult);
        } catch (Exception e) {
            outputView.printError(e.getMessage());
        }

    }

    private Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            if(i + 1 < args.length) {
                map.put(args[i], args[i + 1]);
            }
        }
        return map;
    }

    private Path getDesktopPath() {
        String userHome = System.getProperty("user.home");
        return Path.of(userHome, "Desktop");
    }
}
