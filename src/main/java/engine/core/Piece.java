package engine.core;

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
    public static final byte BorderPiece = 15;

    // Piece colours
    public static final byte White = 0;
    public static final byte Black = 8;

    // Pieces
    public static final byte WhitePawn = Pawn | White;      // 1
    public static final byte WhiteKnight = Knight | White;  // 2
    public static final byte WhiteBishop = Bishop | White;  // 3
    public static final byte WhiteRook = Rook | White;      // 4
    public static final byte WhiteQueen = Queen | White;    // 5
    public static final byte WhiteKing = King | White;      // 6

    public static final byte BlackPawn = Pawn | Black;      // 9
    public static final byte BlackKnight = Knight | Black;  // 10
    public static final byte BlackBishop = Bishop | Black;  // 11
    public static final byte BlackRook = Rook | Black;      // 12
    public static final byte BlackQueen = Queen | Black;    // 13
    public static final byte BlackKing = King | Black;      // 14

    public static String[] pieceCodeToPiece = new String[16];

    public static final byte typeMask = 0b0111;
    public static final byte colourFlag = 0b1000;
    public static final byte castlesFlag = 0b10000;
    public static final byte enPassantFlag = 0b100000;
    public static final byte pinnedFlag = 0b1000000;

    public static boolean isWhitePiece(byte piece) {
        return piece != None && piece != BorderPiece && (piece & Piece.colourFlag) == 0;
    }

    public static boolean isBlackPiece(byte piece) {
        return piece != None && piece != BorderPiece && (piece & Piece.colourFlag) != 0;
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

        pieceCodeToPiece[0] = "None";
        pieceCodeToPiece[1] = "WhitePawn";
        pieceCodeToPiece[2] = "WhiteKnight";
        pieceCodeToPiece[3] = "WhiteBishop";
        pieceCodeToPiece[4] = "WhiteRook";
        pieceCodeToPiece[5] = "WhiteQueen";
        pieceCodeToPiece[6] = "WhiteKing";
        pieceCodeToPiece[9] = "BlackPawn";
        pieceCodeToPiece[10] = "BlackKnight";
        pieceCodeToPiece[11] = "BlackBishop";
        pieceCodeToPiece[12] = "BlackRook";
        pieceCodeToPiece[13] = "BlackQueen";
        pieceCodeToPiece[14] = "BlackKing";
    }
}
