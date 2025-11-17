package teddie.common.config;

import teddie.controller.ProjectGeneratorController;
import teddie.controller.TeDDieController;
import teddie.generator.PackageStatementReplacer;
import teddie.generator.PackageStructureBuilder;
import teddie.generator.ProjectWriter;
import teddie.generator.ReadmeWriter;
import teddie.generator.TemplateCopier;
import teddie.service.MissionService;
import teddie.view.ConsoleView;
import teddie.view.OutputView;
import teddie.common.util.HttpRequestSender;
import teddie.api.RagClient;
import teddie.service.RequestBodyBuilder;

public class AppConfig {
    public OutputView outputView() {
        return new ConsoleView();
    }

    public HttpRequestSender httpRequestSender() {
        return new HttpRequestSender();
    }

    public RagClient ragClient() {
        return new RagClient(httpRequestSender());
    }

    public RequestBodyBuilder requestBodyBuilder() {
        return new RequestBodyBuilder();
    }

    public MissionService missionService() {
        return new MissionService(httpRequestSender(), requestBodyBuilder(), ragClient());
    }

    public PackageStatementReplacer fileReplacer() {
        return new PackageStatementReplacer();
    }

    public ReadmeWriter readmeWriter() {
        return new ReadmeWriter();
    }

    public ProjectWriter projectWriter() {
        return new ProjectWriter(fileReplacer(), readmeWriter());
    }

    public TemplateCopier templateCopier() {
        return new TemplateCopier();
    }

    public PackageStructureBuilder packageStructureBuilder() {
        return new PackageStructureBuilder();
    }

    public ProjectGeneratorController projectGeneratorController() {
        return new ProjectGeneratorController(templateCopier(), packageStructureBuilder(), projectWriter());
    }

    public TeDDieController teDDieController() {
        return new TeDDieController(missionService(), outputView(), projectGeneratorController());
    }
}
