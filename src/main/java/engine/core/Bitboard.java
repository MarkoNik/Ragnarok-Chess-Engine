package engine.core;

import app.EngineLogger;

public class Bitboard {

    public static int PIECE_TYPES = 12;

    // white occupancies, black occupancies, both occupancies
    public static int OCCUPANCY_TYPES = 3;

    private long[] pieces = new long[PIECE_TYPES];
    private long[] occupancies = new long[3];

    public void setPiece(int square, char piece) {
        long bitboard = 1L << square;
        pieces[BitboardHelper.pieceMap.get(piece)] |= bitboard;
    }

    public void makeMove(int move) {
        // TODO
    }

    public void logBoardState() {
        char[] output = new char[64];
        for (int i = 0; i < 64; i++) output[i] = ' ';
        for (int i = 0; i < PIECE_TYPES; i++) {
            long tempBitboard = pieces[i];
            while (tempBitboard != 0) {
                int square = BitboardHelper.getLs1bIndex(tempBitboard);
                tempBitboard = BitboardHelper.popBit(tempBitboard, square);
                output[square] = BitboardHelper.asciiPieces[i];
            }
        }

        StringBuilder sb = new StringBuilder("\n\n");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (j == 0) sb.append("   ").append(8 - i).append("   ");
                sb.append(output[8 * i + j] == ' ' ? '.' : output[8 * i + j]).append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n       a b c d e f g h\n");
        EngineLogger.debug(sb.toString());
    }
}
