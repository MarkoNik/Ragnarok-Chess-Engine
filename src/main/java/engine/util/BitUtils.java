package engine.util;

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
}
