package engine.util;

import engine.core.Bitboard;

import java.util.List;

public class BitboardMoveGenerator {
    /*
    - Move structure:
    0000 0000 0000 0000 0000 1111 1111 - to
    0000 0000 0000 1111 1111 0000 0000 - from
    0000 0000 1111 0000 0000 0000 0000 - piece
    0000 1111 0000 0000 0000 0000 0000 - promotion piece
    0001 0000 0000 0000 0000 0000 0000 - double push flag
    0010 0000 0000 0000 0000 0000 0000 - castles flag
    0100 0000 0000 0000 0000 0000 0000 - en passant flag
    1000 0000 0000 0000 0000 0000 0000 - capture flag
     */
    private Bitboard bitboard;

    public void setBitboard(Bitboard bitboard) {
        this.bitboard = bitboard;
    }

    public List<Integer> generateLegalMoves(boolean isWhiteTurn) {
        // TODO
        return null;
    }

    private int encodeMove(int from,
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

    private int extractFrom(int move) {
        return move & 0xFF;
    }
    private int extractTo(int move) {
        return (move >> 8) & 0xFF;
    }

    private int extractPiece(int move) {
        return (move >> 16) & 0xF;
    }

    private int extractPromotionPiece(int move) {
        return (move >> 20) & 0xF;
    }

    private int extractDoublePushFlag(int move) {
        return (move >> 24) & 1;
    }

    private int extractCastlesFlag(int move) {
        return (move >> 25) & 1;
    }

    private int extractEnPassantFlag(int move) {
        return (move >> 26) & 1;
    }

    private int extractCaptureFlag(int move) {
        return (move >> 27) & 1;
    }
}
