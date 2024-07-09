package engine.core.state;

import java.util.List;

public class GameState {
    private Bitboard bitboard;
    private boolean isWhiteTurn;
    private int halfmoveClock;
    private int fullmoveNumber;

    public GameState(Bitboard bitboard, boolean isWhiteTurn, int halfmoveClock, int fullmoveNumber) {
        this.bitboard = bitboard;
        this.isWhiteTurn = isWhiteTurn;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveNumber = fullmoveNumber;
    }

    public void playMoves(List<Integer> moves) {
        for (int move : moves) {
            bitboard.makeMove(move);
            isWhiteTurn = !isWhiteTurn;
        }
    }

    public void playMove(int move) {
        bitboard.makeMove(move);
        isWhiteTurn = !isWhiteTurn;
    }

    public void logState() {
//        bitboard.logState();
    }

    public Bitboard getBitboard() {
        return bitboard;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }
}
