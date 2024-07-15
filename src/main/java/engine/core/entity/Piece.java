package engine.core.entity;

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

    static {
        pieceCodeToPiece[0] = "White Pawn";
        pieceCodeToPiece[1] = "White Knight";
        pieceCodeToPiece[2] = "White Bishop";
        pieceCodeToPiece[3] = "White Rook";
        pieceCodeToPiece[4] = "White Queen";
        pieceCodeToPiece[5] = "White King";
        pieceCodeToPiece[6] = "Black Pawn";
        pieceCodeToPiece[7] = "Black Knight";
        pieceCodeToPiece[8] = "Black Bishop";
        pieceCodeToPiece[9]  = "Black Rook";
        pieceCodeToPiece[10] = "Black Queen";
        pieceCodeToPiece[11] = "Black King";
    }
}
