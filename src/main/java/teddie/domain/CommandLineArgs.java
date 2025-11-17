package teddie.domain;

import java.util.HashMap;
import java.util.Map;

public class CommandLineArgs {
    private static final String TOPIC_PREFIX = "--topic";
    private static final String DIFFICULTY_PREFIX = "--difficulty";

    private final Map<String, String> args;

    public CommandLineArgs(String[] args) {
        this.args = parseArgs(args);
    }

    private Map<String, String> parseArgs(String[] args) {
        Map<String, String> arg = new HashMap<>();
        for (int i = 0; i + 1 < args.length; i += 2) {
            arg.put(args[i], args[i + 1]);
        }
        return arg;
    }

    public String getTopic() {
        return args.get(TOPIC_PREFIX);
    }

    public String getDifficulty() {
        return args.get(DIFFICULTY_PREFIX);
    }
}
