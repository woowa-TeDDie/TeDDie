package teddie;

import teddie.api.HttpRequestSender;
import teddie.api.RagClient;
import teddie.api.RequestBodyBuilder;
import teddie.controller.TeDDieController;
import teddie.controller.ProjectGeneratorController;
import teddie.service.MissionService;
import teddie.view.ConsoleView;
import teddie.view.OutputView;

public class Application {
    public static void main(String[] args) {
        RequestBodyBuilder bodyBuilder = new RequestBodyBuilder();
        HttpRequestSender httpRequestSender = new HttpRequestSender();
        RagClient ragClient = new RagClient(httpRequestSender);
        MissionService missionService = new MissionService(httpRequestSender, bodyBuilder, ragClient);
        OutputView view = new ConsoleView();
        ProjectGeneratorController projectGeneratorController = new ProjectGeneratorController();

        TeDDieController controller = new TeDDieController(missionService, view, projectGeneratorController);
        controller.run(args);
    }
}