package engine.core.state;

import app.UciLogger;
import engine.core.bitboard.BitboardHelper;
import engine.util.PerftDriver;
import engine.util.bits.BitboardMoveGenerator;
import engine.util.bits.MoveEncoder;
import uci.command.GoCommandWrapper;

import java.util.Map;
import java.util.Random;

public class EngineState {
    private GameState gameState;

    // TODO make a set of available configuration parameters and their values
    private Map<String, String> configMap;
    private int bestMove;
    private BitboardHelper bitboardHelper;
    private BitboardMoveGenerator moveGenerator;

    public EngineState() {
        bitboardHelper = new BitboardHelper();
        moveGenerator = new BitboardMoveGenerator(bitboardHelper);
    }

    public void search(GoCommandWrapper goCommandWrapper) {
        if (goCommandWrapper.perftDepth != -1) {
            PerftDriver driver = new PerftDriver(this, gameState);
            long startTime = System.currentTimeMillis();
            driver.runTest(goCommandWrapper.perftDepth);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Nodes: " + driver.getNodes());
            System.out.println("Elapsed time: " + (double) elapsedTime / 1000);
            return;
        }

        moveGenerator.setBitboard(gameState.getBitboard());
        int[] legalMoves = moveGenerator.generateLegalMoves(gameState.isWhiteTurn());
        int moveCount = moveGenerator.getMoveCounter();
        MoveEncoder.logMovesStats(legalMoves, moveCount);
        for (int i = 0; i < moveCount; i++) {
            MoveEncoder.logMove(legalMoves[i]);
        }
        Random rand = new Random();
        bestMove = legalMoves[rand.nextInt(moveCount)];
        gameState.playMove(bestMove);
        moveGenerator.clearMoves();
    }

    private void perftDriver(int depth) {
        moveGenerator.setBitboard(gameState.getBitboard());
        int[] legalMoves = moveGenerator.generateLegalMoves(gameState.isWhiteTurn());
        int moveCount = moveGenerator.getMoveCounter();
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
