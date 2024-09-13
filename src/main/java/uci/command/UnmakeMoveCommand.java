package uci.command;

import engine.core.state.EngineState;

public class UnmakeMoveCommand implements Command {

    @Override
    public int execute(EngineState engineState) {
        engineState.getGameState().unmakeUciDebugMove();
        engineState.logTTEntry();
        return 0;
    }
}
