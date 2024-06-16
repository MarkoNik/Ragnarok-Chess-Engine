package uci.command;

import engine.EngineState;

public class QuitCommand implements Command {
    @Override
    public int execute(EngineState engineState) {
        return -1;
    }
}
