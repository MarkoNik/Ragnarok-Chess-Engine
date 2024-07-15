package engine.util.perft;

import engine.core.state.EngineState;
import engine.core.state.GameState;
import engine.util.bits.FenParser;
import engine.util.bits.MoveGenerator;

public class PerftDriver {
    private long nodes = 0;
    private EngineState engineState;
    private GameState gameState;
    private MoveGenerator moveGenerator;
//    private int[] moveStack = new int[10000];
//    private int moveStackTop = 0;

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
//        System.out.println("moveStackTop: " + moveStackTop);
//        for (int i = 0; i < moveStackTop; i++) {
//            MoveEncoder.logMove(moveStack[i]);
//        }
//        System.out.println();
        engineState.clearMoves();

//        if (depth == 1) {
//            nodes += moveCounter;
//            return;
//        }

        for (int i = 0; i < moveCounter; i++) {
//            moveStack[moveStackTop++] = moves[i];
            gameState.getBitboard().backupState();
            gameState.playMove(moves[i]);

            search(depth -1);

//            moveStackTop--;
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
