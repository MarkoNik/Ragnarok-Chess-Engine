package engine.core;

import app.EngineLogger;

/**
 * This class is used to represent the state of the board during the game.
 * It will store an integer matrix (laid out as an array) of size 10x12,
 * as it will be beneficial for faster search when doing move generation.
 *
 * Deprecated by bitboards.
 */
public class Board {
    public static final int ROWS = 12;
    public static final int COLS = 10;
    public static final int BOARD_SIZE = ROWS * COLS;
    public static final int BOARD_START = 2 * COLS;
    public static final int BOARD_END = 10 * COLS;

    public byte[] state;

    // store king positions for faster pin calculation in move generator
    public int whiteKingPosition;
    public int blackKingPosition;

    public Board(byte[] state) {
        this.state = state;
    }

    public void makeMove(Move move) {
        EngineLogger.debug("Moving piece: " + state[move.from] + " from square: " + move.from + " to square: " + move.to);
        // update king position if moved
        if (state[move.from] == Piece.WhiteKing) {
            whiteKingPosition = move.to;
        }
        if (state[move.from] == Piece.BlackKing) {
            blackKingPosition = move.to;
        }

        state[move.to] = state[move.from];
        state[move.from] = Piece.None;
    }

    public void logState() {
        StringBuilder sb = new StringBuilder();
        for (int i = BOARD_START; i < BOARD_END; i++) {
            if (state[i] == Piece.BorderPiece) {
                if (i % COLS == 0) {
                    sb.append("\n");
                }
                continue;
            }
            sb.append(state[i]).append(" ");
        }
        EngineLogger.debug(sb.toString());
    }
}
