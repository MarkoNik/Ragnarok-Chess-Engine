package engine.core.entity;

import java.util.HashMap;
import java.util.Map;

public class Piece {

    // Piece types
    public static final byte NONE = 0;
    public static final byte PAWN = 1;
    public static final byte KNIGHT = 2;
    public static final byte BISHOP = 3;
    public static final byte ROOK = 4;
    public static final byte QUEEN = 5;
    public static final byte KING = 6;

    // Pieces
    public static final byte WHITE_PAWN = 0;      // 1
    public static final byte WHITE_KNIGHT = 1;  // 2
    public static final byte WHITE_BISHOP = 2;  // 3
    public static final byte WHITE_ROOK = 3;      // 4
    public static final byte WHITE_QUEEN = 4;    // 5
    public static final byte WHITE_KING = 5;      // 6

    public static final byte BLACK_PAWN = 6;      // 9
    public static final byte BLACK_KNIGHT = 7;  // 10
    public static final byte BLACK_BISHOP = 8;  // 11
    public static final byte BLACK_ROOK = 9;      // 12
    public static final byte BLACK_QUEEN = 10;    // 13
    public static final byte BLACK_KING = 11;      // 14
    public static final byte MAX_PIECE = 12;

    public static String[] pieceCodeToPiece = new String[MAX_PIECE];

    public static final Map<Character, Byte> pieceMap = new HashMap<>();

    static {
        // White pieces
        pieceMap.put('P', Piece.WHITE_PAWN);
        pieceMap.put('N', Piece.WHITE_KNIGHT);
        pieceMap.put('B', Piece.WHITE_BISHOP);
        pieceMap.put('R', Piece.WHITE_ROOK);
        pieceMap.put('Q', Piece.WHITE_QUEEN);
        pieceMap.put('K', Piece.WHITE_KING);

        // Black pieces
        pieceMap.put('p', Piece.BLACK_PAWN);
        pieceMap.put('n', Piece.BLACK_KNIGHT);
        pieceMap.put('b', Piece.BLACK_BISHOP);
        pieceMap.put('r', Piece.BLACK_ROOK);
        pieceMap.put('q', Piece.BLACK_QUEEN);
        pieceMap.put('k', Piece.BLACK_KING);

        // Empty squares
        pieceMap.put(' ', Piece.NONE);

        pieceCodeToPiece[0] = "WhitePawn";
        pieceCodeToPiece[1] = "WhiteKnight";
        pieceCodeToPiece[2] = "WhiteBishop";
        pieceCodeToPiece[3] = "WhiteRook";
        pieceCodeToPiece[4] = "WhiteQueen";
        pieceCodeToPiece[5] = "WhiteKing";
        pieceCodeToPiece[6] = "BlackPawn";
        pieceCodeToPiece[7] = "BlackKnight";
        pieceCodeToPiece[8] = "BlackBishop";
        pieceCodeToPiece[9]  = "BlackRook";
        pieceCodeToPiece[10] = "BlackQueen";
        pieceCodeToPiece[11] = "BlackKing";
    }
}
