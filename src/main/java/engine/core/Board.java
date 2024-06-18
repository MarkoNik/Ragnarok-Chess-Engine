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

    public void makeMove(String move) {
        // Translate the move from algebraic notation to 10x12 coordinates
        int fromIndex = algebraicToIndex(move.substring(0, 2));
        int toIndex = algebraicToIndex(move.substring(2, 4));
        EngineLogger.debug("Moving piece: " + state[fromIndex] + " from square: " + fromIndex + " to square: " + toIndex);

        // Move the piece
        state[toIndex] = state[fromIndex];
        state[fromIndex] = Piece.None;
    }

    private static int algebraicToIndex(String position) {
        int file = position.charAt(0) - 'a' + 1;
        int rank = 10 - (position.charAt(1) - '0');
        return rank * 10 + file;
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
