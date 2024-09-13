package uci.command;

import engine.core.state.EngineState;
import engine.util.bits.FenParser;

import java.util.Arrays;

public class MakeMoveCommand implements Command {
    private final String[] moves;

    public MakeMoveCommand(String line) {
        moves = line.substring(5).trim().split(" ");
    }

    @Override
    public int execute(EngineState engineState) {
        Arrays.stream(moves)
                .map(FenParser::parseUciMove)
                .forEach(engineState.getGameState()::makeUciDebugMove);
        engineState.logTTEntry();
        return 0;
    }
}
