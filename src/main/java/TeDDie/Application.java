package TeDDie;

import TeDDie.api.HttpRequestSender;
import TeDDie.api.RequestBodyBuilder;
import TeDDie.controller.TeDDieController;
import TeDDie.service.MissionService;
import TeDDie.view.ConsoleView;
import TeDDie.view.OutputView;

public class Application {
    public static void main(String[] args) {
        RequestBodyBuilder bodyBuilder = new RequestBodyBuilder();
        HttpRequestSender httpRequestSender = new HttpRequestSender();
        MissionService missionService = new MissionService(httpRequestSender, bodyBuilder);
        OutputView view = new ConsoleView();
        TeDDieController controller = new TeDDieController(missionService, view);
        controller.run(args);
    }
}