package TeDDie;

import TeDDie.api.HttpRequestSender;
import TeDDie.api.RagClient;
import TeDDie.api.RequestBodyBuilder;
import TeDDie.controller.TeDDieController;
import TeDDie.generator.ProjectGenerator;
import TeDDie.service.MissionService;
import TeDDie.view.ConsoleView;
import TeDDie.view.OutputView;

public class Application {
    public static void main(String[] args) {
        RequestBodyBuilder bodyBuilder = new RequestBodyBuilder();
        HttpRequestSender httpRequestSender = new HttpRequestSender();
        RagClient ragClient = new RagClient(httpRequestSender);
        MissionService missionService = new MissionService(httpRequestSender, bodyBuilder, ragClient);
        OutputView view = new ConsoleView();
        ProjectGenerator projectGenerator = new ProjectGenerator();

        TeDDieController controller = new TeDDieController(missionService, view, projectGenerator);
        controller.run(args);
    }
}