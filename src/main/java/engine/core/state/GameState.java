package engine.core.state;

import engine.core.entity.UciMove;

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
            playMove(move);
        }
    }

    public void playUciMoves(List<UciMove> moves) {
        for (UciMove move : moves) {
            playUciMove(move);
        }
    }

    public void playMove(int move) {
        bitboard.makeMove(move, isWhiteTurn, false);
        isWhiteTurn = !isWhiteTurn;
    }

    public void playUciMove(UciMove move) {
        bitboard.makeUciMove(move, isWhiteTurn);
        isWhiteTurn = !isWhiteTurn;
    }

    public void logState() {
        bitboard.logBoardState();
    }

    public Bitboard getBitboard() {
        return bitboard;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public void switchPlayer() {
        isWhiteTurn = !isWhiteTurn;
    }
}
