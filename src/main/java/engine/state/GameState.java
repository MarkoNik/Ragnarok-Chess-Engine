package engine.state;

import engine.core.Board;
import engine.core.Move;

import java.util.List;

public class GameState {
    private Board board;
    private boolean isWhiteTurn = true;
    private String castlingAvailability;
    private String enPassantTargetSquare;
    private int halfmoveClock;
    private int fullmoveNumber;

    public GameState(Board board, boolean isWhiteTurn, String castlingAvailability, String enPassantTargetSquare, int halfmoveClock, int fullmoveNumber) {
        this.board = board;
        this.isWhiteTurn = isWhiteTurn;
        this.castlingAvailability = castlingAvailability;
        this.enPassantTargetSquare = enPassantTargetSquare;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveNumber = fullmoveNumber;
    }

    public void playMoves(List<Move> moves) {
        for (Move move : moves) {
            board.makeMove(move);
            isWhiteTurn = !isWhiteTurn;
        }
    }

    public void playMove(Move move) {
        board.makeMove(move);
        isWhiteTurn = !isWhiteTurn;
    }

    public void logState() {
        board.logState();
    }

    public Board getBoard() {
        return board;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }
}
