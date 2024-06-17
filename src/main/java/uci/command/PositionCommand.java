package uci.command;

import app.Constants;
import app.UciLogger;
import engine.EngineState;
import engine.GameState;
import util.FenParser;

import java.util.Arrays;

public class PositionCommand implements Command {
    private String[] moves;
    // in case fen is not found in the string, we just default to starting position
    private String position = Constants.INITIAL_POS;
    public PositionCommand(String params) {
        String[] split = params.trim().split("moves");
        if (split.length != 2) {
            UciLogger.error("Error parsing position UCI command: moves not received");
        }
        moves = split[1].trim().split(" ");

        if (split[0].contains("fen")) {
            position = split[0].trim().split("fen")[1].trim();
        }
    }

    @Override
    public int execute(EngineState engineState) {
        GameState newState = FenParser.parseFEN(position);
        newState.playMoves(Arrays.asList(moves));
        engineState.setGameState(newState);
        return 0;
    }
}
