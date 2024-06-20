package engine.state;

import engine.core.Board;

import java.util.List;

public class GameState {
    private Board board;
    private char activeColor;
    private String castlingAvailability;
    private String enPassantTargetSquare;
    private int halfmoveClock;
    private int fullmoveNumber;

    public GameState(Board board, char activeColor, String castlingAvailability, String enPassantTargetSquare, int halfmoveClock, int fullmoveNumber) {
        this.board = board;
        this.activeColor = activeColor;
        this.castlingAvailability = castlingAvailability;
        this.enPassantTargetSquare = enPassantTargetSquare;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveNumber = fullmoveNumber;
    }

    public void playMoves(List<String> moves) {
        for (String move : moves) {
            board.makeMove(move);
        }
    }

    public void logState() {
        board.logState();
    }

}
