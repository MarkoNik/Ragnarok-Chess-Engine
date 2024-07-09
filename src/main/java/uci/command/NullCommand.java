package uci.command;

import app.UciLogger;
import engine.core.state.EngineState;

public class NullCommand implements Command {
    @Override
    public int execute(EngineState engineState) {
        UciLogger.warn("Unrecognized command.");
        return 0;
    }
}
