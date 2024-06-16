package uci.command;

import app.UciLogger;
import engine.EngineState;

public class DebugCommand implements Command {
    private boolean on;

    public DebugCommand(String params) {
        String[] split = params.trim().split("\\s++");
        if (split[1].equals("on") || split[1].equals("off")) {
            on = split[1].equals("on");
        }
        else {
            UciLogger.error("Error parsing debug UCI command: neither on nor off specified.");
        }
    }

    @Override
    public int execute(EngineState engineState) {
        // TODO
        return 0;
    }
}
