package engine.core;

import app.EngineLogger;

/**
 * This class is used to represent the state of the board during the game.
 * It will store an integer matrix (laid out as an array) of size 10x12,
 * as it will be beneficial for faster search when doing move generation.
 */
public class Board {
    public static final int ROWS = 10;
    public static final int COLS = 12;
    public static final int BOARD_SIZE = ROWS * COLS;

    public byte[] state;

    public Board(byte[] state) {
        this.state = state;
    }

    public void makeMove(Move move) {
        EngineLogger.debug("Moving piece: " + state[move.from] + " from square: " + move.from + " to square: " + move.to);
        state[move.to] = state[move.from];
        state[move.from] = Piece.None;
    }

    public void logState() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (i % ROWS == 0) {
                sb.append("\n");
            }
            sb.append(state[i]).append(" ");
        }
        EngineLogger.debug(sb.toString());
    }
}
