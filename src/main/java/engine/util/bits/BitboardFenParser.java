package engine.util.bits;

import engine.core.entity.UciMove;
import engine.core.state.Bitboard;
import engine.core.state.GameState;

import static app.Constants.*;

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
        byte castlesFlags = 0;
        castlesFlags |= (byte) (castlingAvailability.contains("K") ? WHITE_KINGSIDE_CASTLES_MASK : 0);
        castlesFlags |= (byte) (castlingAvailability.contains("Q") ? WHITE_QUEENSIDE_CASTLES_MASK : 0);
        castlesFlags |= (byte) (castlingAvailability.contains("k") ? BLACK_KINGSIDE_CASTLES_MASK : 0);
        castlesFlags |= (byte) (castlingAvailability.contains("q") ? BLACK_QUEENSIDE_CASTLES_MASK : 0);
        bitboard.setCastlesFlags(castlesFlags);

        // En passant target square
        String enPassantTargetSquare = parts[3];
        if (enPassantTargetSquare.trim().equals("-")) {
            bitboard.setEnPassantSquare(-1);
        }
        else {
            bitboard.setEnPassantSquare(algebraicToIndex(enPassantTargetSquare));
        }

        // Halfmove clock
        int halfmoveClock = parts.length < 5 ? INF : Integer.parseInt(parts[4]);

        // Fullmove number
        int fullmoveNumber = parts.length < 6 ? INF : Integer.parseInt(parts[5]);

        return new GameState(bitboard, isWhiteTurn, halfmoveClock, fullmoveNumber);
    }

    private static Bitboard parsePieces(String piecePlacement) {
        Bitboard bitboard = new Bitboard();
        String[] rows = piecePlacement.split("/");

        int rowIndex = 0;
        for (String row : rows) {
            int colIndex = 0;
            for (char piece : row.toCharArray()) {
                if (Character.isDigit(piece)) {
                    colIndex += piece - '0';
                } else {
                    bitboard.setPiece(rowIndex * 8 + colIndex, piece);
                    colIndex++;
                }
            }
            rowIndex++;
        }

        return bitboard;
    }

    // Method to parse a UCI move string
    public static UciMove parseUciMove(String move) {
        if (move.equals("0000")) {
            // Handle nullmove
            return new UciMove(-1, -1, false, false, 0);
        }

        int from = algebraicToIndex(move.substring(0, 2));
        int to = algebraicToIndex(move.substring(2, 4));
        boolean potentialDoublePushFlag = false;
        boolean castlesFlag = false;
        int promotionPieceFlags = 0;

        // Check for castling
        if (move.equals("e1g1") || move.equals("e8g8") || move.equals("e1c1") || move.equals("e8c8")) {
            castlesFlag = true;
        }

        // Check for pawn double push (white and black)
        if ((move.charAt(1) == '2' && move.charAt(3) == '4') || (move.charAt(1) == '7' && move.charAt(3) == '5')) {
            potentialDoublePushFlag = true;
        }

        // Check for pawn promotion
        if (move.length() == 5) {
            char promotionPiece = move.charAt(4);
            promotionPieceFlags = pieceCharToFlag(promotionPiece);
        }

        return new UciMove(from, to, potentialDoublePushFlag, castlesFlag, promotionPieceFlags);
    }

    // Convert promotion piece character to flag
    private static int pieceCharToFlag(char piece) {
        switch (piece) {
            case 'q':
                return 1;
            case 'r':
                return 2;
            case 'b':
                return 4;
            case 'n':
                return 8;
            default:
                return 0;
        }
    }

    public static String moveToAlgebraic(int move) {
        int from = move & 0xFF;
        int to = (move >>> 8) & 0xFF;
        System.out.println(from);
        System.out.println(to);
        return indexToAlgebraic(from) + indexToAlgebraic(to);
    }

    // Convert algebraic notation (e.g., "e4") to bitboard index (0..63)
    public static int algebraicToIndex(String algebraic) {
        char file = algebraic.charAt(0);
        char rank = algebraic.charAt(1);
        int fileIndex = file - 'a';
        int rankIndex = rank - '1';
        return (7 - rankIndex) * 8 + fileIndex;
    }

    // Convert bitboard index (0..63) to algebraic notation (e.g., "e4")
    public static String indexToAlgebraic(int index) {
        int fileIndex = index % 8;
        int rankIndex = 7 - index / 8;
        char file = (char) ('a' + fileIndex);
        char rank = (char) ('1' + rankIndex);
        return "" + file + rank;
    }
}
