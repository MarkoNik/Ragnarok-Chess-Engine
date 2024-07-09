package uci.command;

import engine.core.state.EngineState;

public class QuitCommand implements Command {
    @Override
    public int execute(EngineState engineState) {
        return -1;
    }
}
