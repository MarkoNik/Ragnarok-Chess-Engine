package engine.util.bits;

import app.EngineLogger;
import engine.core.entity.Piece;

public class MoveEncoder {
    /*

       - Move structure:
       0000 0000 0000 0000 0000 1111 1111 - from
       0000 0000 0000 1111 1111 0000 0000 - to
       0000 0000 1111 0000 0000 0000 0000 - piece
       0000 1111 0000 0000 0000 0000 0000 - promotion piece
       0001 0000 0000 0000 0000 0000 0000 - double push flag
       0010 0000 0000 0000 0000 0000 0000 - castles flag
       0100 0000 0000 0000 0000 0000 0000 - en passant flag
       1000 0000 0000 0000 0000 0000 0000 - capture flag

     */

    public static int encodeMove(int from,
                           int to,
                           int piece,
                           int promotionPiece,
                           int doublePush,
                           int castles,
                           int enPassant,
                           int capture) {
        return from
                | to << 8
                | piece << 16
                | promotionPiece << 20
                | doublePush << 24
                | castles << 25
                | enPassant << 26
                | capture << 27;
    }

    public static int extractFrom(int move) {
        return move & 0xFF;
    }
    public static int extractTo(int move) {
        return (move >> 8) & 0xFF;
    }

    public static int extractPiece(int move) {
        return (move >> 16) & 0xF;
    }

    public static int extractPromotionPiece(int move) {
        return (move >> 20) & 0xF;
    }

    public static int extractDoublePushFlag(int move) {
        return (move >> 24) & 1;
    }

    public static int extractCastlesFlag(int move) {
        return (move >> 25) & 1;
    }

    public static int extractEnPassantFlag(int move) {
        return (move >> 26) & 1;
    }

    public static int extractCaptureFlag(int move) {
        return (move >> 27) & 1;
    }

    public static void logMove(int move) {
        int from = extractFrom(move);
        int to = extractTo(move);
        int piece = extractPiece(move);
        int promotionPiece = extractPromotionPiece(move);
        int doublePushFlag = extractDoublePushFlag(move);
        int castlesFlag = extractCastlesFlag(move);
        int enPassantFlag = extractEnPassantFlag(move);
        int captureFlag = extractCaptureFlag(move);
        StringBuilder sb = new StringBuilder();
        sb.append("Move:\n")
                .append("from: " + BitboardFenParser.indexToAlgebraic(from))
                .append(" | to: " + BitboardFenParser.indexToAlgebraic(to))
                .append(" | piece: " + Piece.pieceCodeToPiece[piece])
                .append(" | promotionPiece: " + Piece.pieceCodeToPiece[promotionPiece])
                .append(" | doublePush: " + doublePushFlag)
                .append(" | castles: " + castlesFlag)
                .append(" | enPassant: " + enPassantFlag)
                .append(" | capture: " + captureFlag)
                .append("\n");
        EngineLogger.debug(sb.toString());
    }
}
