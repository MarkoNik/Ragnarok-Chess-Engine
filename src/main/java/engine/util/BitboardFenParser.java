package engine.util;

import engine.core.Bitboard;
import engine.state.GameState;

public class BitboardFenParser {
    public static GameState parseFEN(String fenString) {
        String[] parts = fenString.split(" ");

        // Piece placement is the first part
        String piecePlacement = parts[0];
        Bitboard bitboard = parsePieces(piecePlacement);

        // Active color (w or b)
        char activeColor = parts[1].charAt(0);
        boolean isWhiteTurn = true;
        if (activeColor == 'b') {
            isWhiteTurn = false;
        }

        // Castling availability
        String castlingAvailability = parts[2];

        // En passant target square
        String enPassantTargetSquare = parts[3];

        // Halfmove clock
        int halfmoveClock = Integer.parseInt(parts[4]);

        // Fullmove number
        int fullmoveNumber = Integer.parseInt(parts[5]);

        return new GameState(null, isWhiteTurn, castlingAvailability, enPassantTargetSquare, halfmoveClock, fullmoveNumber);
    }

    private static Bitboard parsePieces(String piecePlacement) {
        Bitboard bitboard = new Bitboard();
        String[] rows = piecePlacement.split("/");

        int rowIndex = 7;
        for (String row : rows) {
            int colIndex = 0;
            for (char c : row.toCharArray()) {
                if (Character.isDigit(c)) {
                    colIndex += c - '0';
                } else {
                    bitboard.setPiece(rowIndex * 8 + colIndex, c);
                    colIndex++;
                }
            }
            rowIndex--;
        }

        bitboard.logBoardState();
        return bitboard;
    }

    public static int algebraicToMove(String move) {
        int fromSquare = algebraicToIndex(move.substring(0, 2));
        int toSquare = algebraicToIndex(move.substring(2, 4));
        short packedMove = (short) fromSquare;
        packedMove <<= 8;
        packedMove |= toSquare;
        return packedMove;
    }

    public static String moveToAlgebraic(int move) {
        int from = move >>> 8;
        int to = move & 0xFF;
        return indexToAlgebraic(from) + indexToAlgebraic(to);
    }

    public static short algebraicToIndex(String position) {
        int file = position.charAt(0) - 'a';
        int rank = 8 - (position.charAt(1) - '0');
        return (short) (rank * 8 + file);
    }

    public static String indexToAlgebraic(int square) {
        int file = square % 8;
        int rank = 8 - (square / 8);
        char fileChar = (char) ('a' + file);
        char rankChar = (char) ('0' + rank);
        return "" + fileChar + rankChar;
    }
}
