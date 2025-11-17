package teddie.view;

public class ConsoleView implements OutputView {
    private static final String SUCCESS_MESSAGE = "🧸 TeDDie: 문제 생성 완료!";
    private static final String MISSION_HEADER = "--- (미션 내용) ---";
    private static final String MISSION_FOOTER = "____________________";

    @Override
    public void printMission(String markdown) {
        System.out.println(SUCCESS_MESSAGE);
        System.out.println(MISSION_HEADER);
        System.out.println(markdown);
        System.out.println(MISSION_FOOTER);
    }

    @Override
    public void printError(String errorMessage) {
        System.out.println(errorMessage);
    }
}
