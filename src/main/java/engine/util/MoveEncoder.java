package engine.util;

public class MoveEncoder {

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
}
