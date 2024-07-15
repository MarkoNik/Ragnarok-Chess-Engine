package engine.util.perft;

import engine.core.state.EngineState;
import engine.core.state.GameState;

public class PerftDriver {
    private long nodes = 0;
    private EngineState engineState;
    private GameState gameState;
    private int[] moveStack = new int[100000];
    private int moveStackTop = 0;

    public PerftDriver(EngineState engineState, GameState gameState) {
        this.engineState = engineState;
        this.gameState = gameState;
    }

    public void runTest(int depth) {
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
            moveStack[moveStackTop++] = moves[i];
            gameState.getBitboard().backupState();
            gameState.playMove(moves[i]);

            runTest(depth -1);

            moveStackTop--;
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
