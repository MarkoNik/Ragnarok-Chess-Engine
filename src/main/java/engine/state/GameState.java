package engine.state;

import engine.core.Bitboard;

import java.util.List;

public class GameState {
    // 10x12 Board, deprecated by bitboards.
//    private Board board;
    private Bitboard bitboard;
    private boolean isWhiteTurn = true;
    private String castlingAvailability;
    private String enPassantTargetSquare;
    private int halfmoveClock;
    private int fullmoveNumber;

    public GameState(Bitboard bitboard, boolean isWhiteTurn, String castlingAvailability, String enPassantTargetSquare, int halfmoveClock, int fullmoveNumber) {
        this.bitboard = bitboard;
        this.isWhiteTurn = isWhiteTurn;
        this.castlingAvailability = castlingAvailability;
        this.enPassantTargetSquare = enPassantTargetSquare;
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
