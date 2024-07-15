package uci.command;

import app.Constants;
import engine.core.state.EngineState;
import engine.core.state.GameState;
import engine.util.bits.FenParser;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PositionCommand implements Command {
    private String[] moves = null;
    // in case fen is not found in the string, we just default to starting position
    private String position = Constants.INITIAL_FEN;
    public PositionCommand(String params) {
        String[] split = params.trim().split("moves");
        if (split.length == 2) {
            moves = split[1].trim().split(" ");
        }

        if (split[0].contains("fen")) {
            position = split[0].trim().split("fen")[1].trim();
        }
    }

    @Override
    public int execute(EngineState engineState) {
        GameState newState = FenParser.parseFEN(position);
        if (moves != null) {
            newState.playUciMoves(Arrays.stream(moves).map(FenParser::parseUciMove).collect(Collectors.toList()));
        }
        newState.logState();
        engineState.setGameState(newState);
        return 0;
    }
}
