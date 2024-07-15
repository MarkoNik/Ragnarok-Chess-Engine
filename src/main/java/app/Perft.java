package app;

import engine.core.state.EngineState;
import engine.core.state.GameState;
import engine.util.bits.FenParser;
import engine.util.bits.MoveEncoder;

import java.util.Scanner;

public class Perft {
    private static long nodes = 0;
    private static EngineState engineState;
    private static GameState gameState;
    private static int[] moveStack = new int[100];
    private static int moveStackTop = 0;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String fen = scanner.nextLine();
        gameState = FenParser.parseFEN(fen);
        engineState = new EngineState();
        engineState.setGameState(gameState);
        try {
            long startTime = System.currentTimeMillis();
            perftDriver(6);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Nodes: " + nodes);
            System.out.println("Elapsed time: " + (double) elapsedTime / 1000);
        } catch (Exception e) {
            e.printStackTrace();
            for (int i = 0; i < moveStackTop; i++) {
                MoveEncoder.logMove(moveStack[i]);
            }
        }
    }

    private static void perftDriver(int depth) {
        int[] moves = engineState.generateLegalMoves().clone();
        int moveCounter = engineState.getMoveCounter();
//        System.out.println("moveStackTop: " + moveStackTop);
//        for (int i = 0; i < moveStackTop; i++) {
//            MoveEncoder.logMove(moveStack[i]);
//        }
//        System.out.println();
        engineState.clearMoves();

        if (depth == 1) {
            nodes += moveCounter;
            return;
        }

        for (int i = 0; i < moveCounter; i++) {
            moveStack[moveStackTop++] = moves[i];
            gameState.getBitboard().backupState();
            gameState.playMove(moves[i]);

            perftDriver(depth -1);

            moveStackTop--;
            gameState.getBitboard().restoreState();
            gameState.switchPlayer();
        }
    }
}
