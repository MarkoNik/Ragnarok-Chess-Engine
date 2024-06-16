package uci.command;

import app.UciLogger;
import engine.EngineState;

public class NullCommand implements Command {
    @Override
    public int execute(EngineState engineState) {
        UciLogger.warn("Received unrecognized command.");
        return 0;
    }
}
