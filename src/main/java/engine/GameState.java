package engine;

public class GameState {
    String piecePlacement;
    char activeColor;
    String castlingAvailability;
    String enPassantTargetSquare;
    int halfmoveClock;
    int fullmoveNumber;

    public GameState(String piecePlacement, char activeColor, String castlingAvailability, String enPassantTargetSquare, int halfmoveClock, int fullmoveNumber) {
        this.piecePlacement = piecePlacement;
        this.activeColor = activeColor;
        this.castlingAvailability = castlingAvailability;
        this.enPassantTargetSquare = enPassantTargetSquare;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveNumber = fullmoveNumber;
    }

}
