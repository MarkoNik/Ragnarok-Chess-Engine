package engine.core.state;

import app.Constants;
import app.UciLogger;
import engine.core.bitboard.BitboardHelper;
import engine.search.Evaluator;
import engine.search.Minimax;
import engine.search.MoveGenerator;
import engine.util.PerftDriver;
import engine.util.bits.FenParser;
import uci.command.GoCommandWrapper;

import java.util.List;
import java.util.Map;

import static app.Constants.INF;

public class EngineState {
    private GameState gameState;

    // TODO make a set of available configuration parameters and their values
    private Map<String, String> configMap;
    private int bestMove;
    private final MoveGenerator moveGenerator;
    private final Minimax minimax;
    private final TranspositionTable transpositionTable;

    public EngineState() {
        BitboardHelper bitboardHelper = new BitboardHelper();
        moveGenerator = new MoveGenerator(bitboardHelper);
        Evaluator evaluator = new Evaluator();
        transpositionTable = new TranspositionTable();
        minimax = new Minimax(moveGenerator, evaluator, transpositionTable);
        gameState = FenParser.parseFEN(Constants.INITIAL_FEN);
    }

    public void search(GoCommandWrapper goCommandWrapper) {
        if (goCommandWrapper.perftDepth != -1) {
            PerftDriver perftDriver = new PerftDriver(this, gameState, moveGenerator);
            perftDriver.runPerftTest(goCommandWrapper.perftDepth);
            return;
        }
        int depth = goCommandWrapper.depth;
        if (depth == -1) depth = 5;

        moveGenerator.setBitboard(gameState.getBitboard());
        minimax.setBitboard(gameState.getBitboard());

        int eval = minimax.search(depth, -INF, INF, gameState.isWhiteTurn());
        System.out.print("info score cp " + eval + " ");

        List<Integer> pv = minimax.getPrincipalVariation(gameState.isWhiteTurn());
        System.out.print("pv ");
        for (int move : pv) {
            System.out.print(FenParser.moveToAlgebraic(move) + " ");
        }
        System.out.println();

        bestMove = pv.get(0);
        gameState.playMove(bestMove);
        moveGenerator.clearMoves();
    }

    public int[] generateLegalMoves() {
        moveGenerator.setBitboard(gameState.getBitboard());
        return moveGenerator.generateLegalMoves(gameState.isWhiteTurn());
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
