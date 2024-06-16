package uci.command.receive;

import app.LoggerUtil;
import uci.command.Command;

public class DebugCommand implements Command {
    private boolean on;

    public DebugCommand(String params) {
        String[] split = params.trim().split("\\s++");
        if (split[1].equals("on") || split[1].equals("off")) {
            on = split[1].equals("on");
        }
        else {
            LoggerUtil.error("Error parsing debug UCI command: neither on nor off specified");
        }
    }

    @Override
    public int execute() {
        // TODO
        return 0;
    }
}
