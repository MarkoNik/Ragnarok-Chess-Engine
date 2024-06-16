package util;

import engine.GameState;

// TODO better parsing
public class FenParser {
    public static GameState parseFEN(String fenString) {
        String[] parts = fenString.split(" ");

        // Piece placement is the first part
        String piecePlacement = parts[0];

        // Active color (w or b)
        char activeColor = parts[1].charAt(0);

        // Castling availability
        String castlingAvailability = parts[2];

        // En passant target square
        String enPassantTargetSquare = parts[3];

        // Halfmove clock
        int halfmoveClock = Integer.parseInt(parts[4]);

        // Fullmove number
        int fullmoveNumber = Integer.parseInt(parts[5]);

        return new GameState(piecePlacement, activeColor, castlingAvailability, enPassantTargetSquare, halfmoveClock, fullmoveNumber);
    }

}
