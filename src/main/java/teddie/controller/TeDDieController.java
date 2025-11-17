package teddie.controller;

import teddie.domain.CommandLineArgs;
import teddie.domain.Difficulty;
import teddie.domain.Topic;
import teddie.service.MissionService;
import teddie.view.OutputView;

public class TeDDieController {
    private static final String PROJECT_PREFIX = "java";

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

        String projectName = PROJECT_PREFIX + topic.getValue();
        projectGeneratorController.createProject(projectName, topic.getValue(), projectName);
    }
}
