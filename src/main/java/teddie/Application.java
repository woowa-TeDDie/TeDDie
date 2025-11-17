package teddie;

import teddie.common.config.AppConfig;
import teddie.controller.TeDDieController;

public class Application {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        TeDDieController controller = appConfig.teDDieController();
        controller.run(args);
    }
}
