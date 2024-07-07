package engine.util;

import app.EngineLogger;

public class BitUtils {

    public static boolean getBit(long bitboard, int square) {
        return (bitboard & (1L << square)) != 0;
    }

    public static long setBit(long bitboard, int square) {
        return bitboard | (1L << square);
    }

    public static long popBit(long bitboard, int square) {
        return getBit(bitboard, square) ? bitboard ^ (1L << square) : bitboard;
    }

    public static int getLs1bIndex(long bitboard) {
        if (bitboard != 0) {
            return Long.bitCount((bitboard & -bitboard) - 1);
        }
        return -1;
    }

    public static void logBitboard(long bitboard) {
        StringBuilder sb = new StringBuilder("\n\n");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (j == 0) sb.append("   ").append(8 - i).append("   ");
                sb.append(BitUtils.getBit(bitboard, 8 * i + j) ? 1 : 0).append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n       a b c d e f g h\n");
        EngineLogger.debug(sb.toString());
    }
}
