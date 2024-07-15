package engine.core.state;

import app.UciLogger;
import engine.core.bitboard.BitboardHelper;
import engine.search.Evaluator;
import engine.search.Minimax;
import engine.search.MoveGenerator;
import engine.util.perft.PerftDriver;
import uci.command.GoCommandWrapper;

import java.util.Map;

import static app.Constants.INF;

public class EngineState {
    private GameState gameState;

    // TODO make a set of available configuration parameters and their values
    private Map<String, String> configMap;
    private int bestMove;
    private BitboardHelper bitboardHelper;
    private MoveGenerator moveGenerator;
    private Evaluator evaluator;
    private Minimax minimax;

    public EngineState() {
        bitboardHelper = new BitboardHelper();
        moveGenerator = new MoveGenerator(bitboardHelper);
        evaluator = new Evaluator();
        minimax = new Minimax(moveGenerator, evaluator);
    }

    public void search(GoCommandWrapper goCommandWrapper) {
        if (goCommandWrapper.perftDepth != -1) {
            PerftDriver perftDriver = new PerftDriver(this, gameState, moveGenerator);
            perftDriver.runPerftTest(goCommandWrapper.perftDepth);
            return;
        }

        moveGenerator.setBitboard(gameState.getBitboard());
        minimax.setBitboard(gameState.getBitboard());
        minimax.search(6, -INF, INF, gameState.isWhiteTurn());
        bestMove = minimax.getBestMove();
        gameState.playMove(bestMove);
        moveGenerator.clearMoves();
    }

    public int[] generateLegalMoves() {
        moveGenerator.setBitboard(gameState.getBitboard());
        int[] legalMoves = moveGenerator.generateLegalMoves(gameState.isWhiteTurn());
        return legalMoves;
    }

    public int getMoveCounter() {
        return moveGenerator.getMoveCounter();
    }

    public void clearMoves() {
        moveGenerator.clearMoves();
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

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
