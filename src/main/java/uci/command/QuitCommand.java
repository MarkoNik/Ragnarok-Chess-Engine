package uci.command;

import engine.state.EngineState;

public class QuitCommand implements Command {
    @Override
    public int execute(EngineState engineState) {
        return -1;
    }
}
