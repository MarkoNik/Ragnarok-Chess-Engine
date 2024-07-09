package engine.core.entity;

import java.util.HashMap;
import java.util.Map;

public class Piece {

    // Piece types
    public static final byte None = 0;
    public static final byte Pawn = 1;
    public static final byte Knight = 2;
    public static final byte Bishop = 3;
    public static final byte Rook = 4;
    public static final byte Queen = 5;
    public static final byte King = 6;
    public static final byte MaxPiece = 12;

    // Pieces
    public static final byte WhitePawn = 0;      // 1
    public static final byte WhiteKnight = 1;  // 2
    public static final byte WhiteBishop = 2;  // 3
    public static final byte WhiteRook = 3;      // 4
    public static final byte WhiteQueen = 4;    // 5
    public static final byte WhiteKing = 5;      // 6

    public static final byte BlackPawn = 6;      // 9
    public static final byte BlackKnight = 7;  // 10
    public static final byte BlackBishop = 8;  // 11
    public static final byte BlackRook = 9;      // 12
    public static final byte BlackQueen = 10;    // 13
    public static final byte BlackKing = 11;      // 14

    public static String[] pieceCodeToPiece = new String[MaxPiece];

    public static final byte typeMask = 0b0111;
    public static final byte colourFlag = 0b1000;
    public static final byte castlesFlag = 0b10000;
    public static final byte enPassantFlag = 0b100000;
    public static final byte pinnedFlag = 0b1000000;

    public static boolean isWhitePiece(byte piece) {
        return piece != None && piece != MaxPiece && (piece & Piece.colourFlag) == 0;
    }

    public static boolean isBlackPiece(byte piece) {
        return piece != None && piece != MaxPiece && (piece & Piece.colourFlag) != 0;
    }

    public static final Map<Character, Byte> pieceMap = new HashMap<>();

    static {
        // White pieces
        pieceMap.put('P', Piece.WhitePawn);
        pieceMap.put('N', Piece.WhiteKnight);
        pieceMap.put('B', Piece.WhiteBishop);
        pieceMap.put('R', Piece.WhiteRook);
        pieceMap.put('Q', Piece.WhiteQueen);
        pieceMap.put('K', Piece.WhiteKing);

        // Black pieces
        pieceMap.put('p', Piece.BlackPawn);
        pieceMap.put('n', Piece.BlackKnight);
        pieceMap.put('b', Piece.BlackBishop);
        pieceMap.put('r', Piece.BlackRook);
        pieceMap.put('q', Piece.BlackQueen);
        pieceMap.put('k', Piece.BlackKing);

        // Empty squares
        pieceMap.put(' ', Piece.None);

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
