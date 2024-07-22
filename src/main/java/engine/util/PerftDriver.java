package engine.util;

import engine.core.state.EngineState;
import engine.core.state.GameState;
import engine.search.MoveGenerator;
import engine.util.bits.FenParser;

public class PerftDriver {
    private long nodes = 0;
    private EngineState engineState;
    private GameState gameState;
    private MoveGenerator moveGenerator;

    public PerftDriver(EngineState engineState, GameState gameState, MoveGenerator moveGenerator) {
        this.engineState = engineState;
        this.gameState = gameState;
        this.moveGenerator = moveGenerator;
    }

    public void runPerftTest(int depth) {
        long startTime = System.currentTimeMillis();

        moveGenerator.setBitboard(gameState.getBitboard());
        int[] legalMoves = moveGenerator.generateLegalMoves(gameState.isWhiteTurn()).clone();
        int moveCount = moveGenerator.getMoveCounter();
        moveGenerator.clearMoves();

        long total = 0;
        for (int i = 0; i < moveCount; i++) {
            gameState.getBitboard().backupState();
            gameState.playMove(legalMoves[i]);
            search(depth - 1);
            total += nodes;
            System.out.println(FenParser.moveToAlgebraic(legalMoves[i]) + ": " + nodes);
            nodes = 0;
            gameState.getBitboard().restoreState();
            gameState.switchPlayer();
        }

        System.out.println("\nTotal nodes searched: " + total);
        long endTime = System.currentTimeMillis();
        double elapsedTime = (double) (endTime - startTime) / 1000;
        System.out.println("Elapsed time: " + elapsedTime);
        System.out.println("Nodes per second: " + (double) total / elapsedTime);
    }

    public void search(int depth) {
        if (depth == 0) {
            nodes++;
            return;
        }

        int[] moves = engineState.generateLegalMoves().clone();
        int moveCounter = engineState.getMoveCounter();
        engineState.clearMoves();

//        if (depth == 1) {
//            nodes += moveCounter;
//            return;
//        }

        for (int i = 0; i < moveCounter; i++) {

            gameState.getBitboard().backupState();
            gameState.playMove(moves[i]);

//            long incrementalHash = gameState.getBitboard().getHash();
//            gameState.getBitboard().generateHash(gameState.isWhiteTurn());
//            long fullHash = gameState.getBitboard().getHash();
//
//            if (incrementalHash != fullHash) {
//                EngineLogger.error("Incremental hashing error!"
//                        + "\nIncremental hash: " + incrementalHash
//                        + "\nFull hash: " + fullHash
//                        + "\nDepth: " + depth);
//                MoveEncoder.logMove(moves[i]);
//                Zobrist.logError(incrementalHash, fullHash);
//            }

            search(depth -1);

            gameState.getBitboard().restoreState();
            gameState.switchPlayer();
        }
    }

    public long getNodes() {
        return nodes;
    }

    public void resetNodes() {
        nodes = 0;
    }
}
