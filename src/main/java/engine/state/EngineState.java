package engine.state;

import app.UciLogger;
import engine.util.BitboardMoveGenerator;
import uci.command.GoCommandWrapper;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class EngineState {
    private GameState gameState;

    // TODO make a set of available configuration parameters and their values
    private Map<String, String> configMap;
    private int bestMove;
    private BitboardMoveGenerator moveGenerator = new BitboardMoveGenerator();

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    public void search(GoCommandWrapper goCommandWrapper) {
        moveGenerator.setBitboard(gameState.getBitboard());
        List<Integer> legalMoves = moveGenerator.generateLegalMoves(gameState.isWhiteTurn());
        Random rand = new Random();
        gameState.playMove(bestMove);
    }

    public String getConfigOption(String name) {
        if (!configMap.containsKey(name)) {
            UciLogger.warn("Option with the name: " + name + " is requested but has not been set.");
        }
        return configMap.get(name);
    }
    public void setConfigOption(String name, String value) {
        configMap.put(name, value);
    }

    public int getBestMove() {
        return bestMove;
    }
}
