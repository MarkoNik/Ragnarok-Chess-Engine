package engine.core;

import app.EngineLogger;

/**
 * This class is used to represent the state of the board during the game.
 * It will store an integer matrix (laid out as an array) of size 10x12,
 * as it will be beneficial for faster search when doing move generation.
 */
public class Board {
    private byte[] state;

    public Board(byte[] state) {
        this.state = state;
    }

    public void makeMove(Move move) {
        // Translate the move from algebraic notation to 10x12 coordinates

        EngineLogger.debug("Moving piece: " + state[move.from] + " from square: " + move.from + " to square: " + move.to);

        // Move the piece
        state[move.to] = state[move.from];
        state[move.from] = Piece.None;
    }

    public void logState() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 120; i++) {
            if (i % 10 == 0) {
                sb.append("\n");
            }
            sb.append(state[i]).append(" ");
        }
        EngineLogger.debug(sb.toString());
    }
}
