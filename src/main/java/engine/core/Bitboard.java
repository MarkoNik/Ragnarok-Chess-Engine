package engine.core;

import app.Constants;
import app.EngineLogger;

import java.util.Random;

public class Bitboard {

    /**
    * Bitmask where every bit is set apart from bits on the A file (first column)
    */
    private final long NOT_A_FILE_MASK = -72340172838076674L;

    /**
     * Bitmask where every bit is set apart from bits on the AB files (first and second column)
     */
    private final long NOT_AB_FILE_MASK = -217020518514230020L;

    /**
     * Bitmask where every bit is set apart from bits on the H file (first column)
     */
    private final long NOT_H_FILE_MASK = 9187201950435737471L;

    /**
     * Bitmask where every bit is set apart from bits on the HG files (first and second column)
     */
    private final long NOT_HG_FILE_MASK = 4557430888798830399L;

    private final int WHITE = 0;
    private final int BLACK = 1;
    private final int BOARD_SIZE = Constants.BOARD_SIZE;

    /**
     * The maximum number of pieces that can block a sliding piece's attack
     * (6 in both directions for rooks and bishops)
     */
    private final int MAXIMUM_BLOCKING_PIECES = 12;
    private final int MAXIMUM_BLOCKING_PIECES_MASK = 1 << MAXIMUM_BLOCKING_PIECES;

    private final long[][] pawnAttacks = new long[2][BOARD_SIZE];
    private final long[] knightAttacks = new long[BOARD_SIZE];
    private final long[] bishopAttacks = new long[BOARD_SIZE];
    private final long[] rookAttacks = new long[BOARD_SIZE];
    private final long[] queenAttacks = new long[BOARD_SIZE];
    private final long[] kingAttacks = new long[BOARD_SIZE];

    private final long[][] bishopOccupancies = new long[BOARD_SIZE][MAXIMUM_BLOCKING_PIECES_MASK];
    private final long[][] rookOccupancies = new long[BOARD_SIZE][MAXIMUM_BLOCKING_PIECES_MASK];
    private final long[][] bishopAttacksWithBlockers = new long[BOARD_SIZE][MAXIMUM_BLOCKING_PIECES_MASK];
    private final long[][] rookAttacksWithBlockers = new long[BOARD_SIZE][MAXIMUM_BLOCKING_PIECES_MASK];

    private final long[] bishopMagics = new long[BOARD_SIZE];
    private final long[] rookMagics = new long[BOARD_SIZE];

    public void fillAttackTables() {
        // precalculate attack masks for all leaper pieces
        for (int i = 0; i < BOARD_SIZE; i++) {
            pawnAttacks[WHITE][i] = generatePawnAttacks(WHITE, i);
            pawnAttacks[BLACK][i] = generatePawnAttacks(BLACK, i);
            knightAttacks[i] = generateKnightAttacks(i);
            kingAttacks[i] = generateKingAttacks(i);
        }

        // precalculate occupancies and attacks with blockers for sliding pieces
        for (int i = 0; i < BOARD_SIZE; i++) {
            bishopAttacks[i] = generateBishopAttacks(i);
            rookAttacks[i] = generateRookAttacks(i);
            for (int j = 0; j < MAXIMUM_BLOCKING_PIECES_MASK; j++) {
                bishopOccupancies[i][j] = getOccupancy(j, bishopAttacks[i]);
                bishopAttacksWithBlockers[i][j] = generateBishopAttacksWithBlockers(i, bishopOccupancies[i][j]);
                rookOccupancies[i][j] = getOccupancy(j, rookAttacks[i]);
                rookAttacksWithBlockers[i][j] = generateRookAttacksWithBlockers(i, rookOccupancies[i][j]);
            }
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            bishopMagics[i] = generateBishopMagic(i);
            for (int j = 0; j < 100; j++) {
                long newMagic = generateBishopMagic(i);
                if (getLs1bIndex(bishopMagics[i]) > getLs1bIndex(newMagic))
                    bishopMagics[i] = newMagic;
            }
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            rookMagics[i] = generateRookMagic(i);
            for (int j = 0; j < 100; j++) {
                long newMagic = generateRookMagic(i);
                if (getLs1bIndex(rookMagics[i]) > getLs1bIndex(newMagic))
                    rookMagics[i] = newMagic;
            }
        }

        System.out.println("Bishop magics\n");
        for (int i = 0; i < 64; i++) {
            System.out.println(bishopMagics[i]);
        }

        System.out.println("\nRook magics\n");
        for (int i = 0; i < 64; i++) {
            System.out.println(rookMagics[i]);
        }
    }

    public long generateBishopMagic(int square) {
        Random random = new Random(123456);
        int relevantBits = Long.bitCount(bishopAttacks[square]);

        for (int iterations = 0; iterations < 1e8; iterations++) {

            long magicCandidate = random.nextLong() & random.nextLong() & random.nextLong();
            long[] attackMap = new long[4096];
            boolean badCollision = false;

            for (int i = 0; i < MAXIMUM_BLOCKING_PIECES_MASK; i++) {
                int magicIndex = (int)((bishopOccupancies[square][i] * magicCandidate) >>> (64 - relevantBits));
                if (attackMap[magicIndex] == 0L) {
                    attackMap[magicIndex] = bishopAttacksWithBlockers[square][i];
                }
                else if (attackMap[magicIndex] != bishopAttacksWithBlockers[square][i]) {
                    badCollision = true;
                }
            }

            if (!badCollision) {
                return magicCandidate;
            }
        }
        EngineLogger.error("Could not find magic number.");
        return -1;
    }
    public long generateRookMagic(int square) {
        Random random = new Random(123456);
        int relevantBits = Long.bitCount(rookAttacks[square]);

        for (int iterations = 0; iterations < 1e9; iterations++) {

            long magicCandidate = random.nextLong() & random.nextLong() & random.nextLong();
            long[] attackMap = new long[4096];
            boolean badCollision = false;

            for (int i = 0; i < MAXIMUM_BLOCKING_PIECES_MASK; i++) {
                int magicIndex = (int)((rookOccupancies[square][i] * magicCandidate) >>> (64 - relevantBits));
                if (attackMap[magicIndex] == 0L) {
                    attackMap[magicIndex] = rookAttacksWithBlockers[square][i];
                }
                else if (attackMap[magicIndex] != rookAttacksWithBlockers[square][i]) {
                    badCollision = true;
                }
            }

            if (!badCollision) {
                return magicCandidate;
            }
        }
        EngineLogger.error("Could not find magic number.");
        return -1;
    }

    /**
     * This function is used to calculate all the squares that the pawn
     * with the given color is attacking from the given square.
     * @param color Player color
     * @param square Pawn square
     * @return knight attack bitboard
     */
    private long generatePawnAttacks(int color, int square) {
        long attacks = 0L;
        long bitboard = 1L << square;

        if (color == WHITE) {
            if (((bitboard >>> 7) & NOT_A_FILE_MASK) != 0) {
                attacks |= (bitboard >>> 7);
            }
            if (((bitboard >>> 9) & NOT_H_FILE_MASK) != 0) {
                attacks |= (bitboard >>> 9);
            }
        }
        else {
            if (((bitboard << 7) & NOT_H_FILE_MASK) != 0) {
                attacks |= (bitboard << 7);
            }
            if (((bitboard << 9) & NOT_A_FILE_MASK) != 0) {
                attacks |= (bitboard << 9);
            }
        }
        return attacks;
    }

    /**
     * This function is used to calculate all the squares that the knight
     * can jump to from the given square.
     * @param square Knight square
     * @return knight attack bitboard
     */
    private long generateKnightAttacks(int square) {
        long attacks = 0L;
        long bitboard = 1L << square;

        if (((bitboard >>> 17) & NOT_H_FILE_MASK) != 0) {
            attacks |= (bitboard >>> 17);
        }
        if (((bitboard >>> 15) & NOT_A_FILE_MASK) != 0) {
            attacks |= (bitboard >>> 15);
        }
        if (((bitboard >>> 10) & NOT_HG_FILE_MASK) != 0) {
            attacks |= (bitboard >>> 10);
        }
        if (((bitboard >>> 6) & NOT_AB_FILE_MASK) != 0) {
            attacks |= (bitboard >>> 6);
        }

        if (((bitboard << 17) & NOT_A_FILE_MASK) != 0) {
            attacks |= (bitboard << 17);
        }
        if (((bitboard << 15) & NOT_H_FILE_MASK) != 0) {
            attacks |= (bitboard << 15);
        }
        if (((bitboard << 10) & NOT_AB_FILE_MASK) != 0) {
            attacks |= (bitboard << 10);
        }
        if (((bitboard << 6) & NOT_HG_FILE_MASK) != 0) {
            attacks |= (bitboard << 6);
        }

        return attacks;
    }

    /**
     * This function is used to calculate all the squares that the bishop
     * can see from the given square, ignoring blocekrs.
     * @param square Bishop square
     * @return bishop attack bitboard
     */
    private long generateBishopAttacks(int square) {
        long attacks = 0L;
        int tr = square / 8;
        int tf = square % 8;

        int r, f;
        for (r = tr + 1, f = tf + 1; r <= 6 && f <= 6; r++, f++) {
            attacks |= (1L << (r * 8 + f));
        }
        for (r = tr - 1, f = tf + 1; r >= 1 && f <= 6; r--, f++) {
            attacks |= (1L << (r * 8 + f));
        }
        for (r = tr + 1, f = tf - 1; r <= 6 && f >= 1; r++, f--) {
            attacks |= (1L << (r * 8 + f));
        }
        for (r = tr - 1, f = tf - 1; r >= 1 && f >= 1; r--, f--) {
            attacks |= (1L << (r * 8 + f));
        }

        return attacks;
    }

    /**
     * This function is used to calculate all the squares that the bishop
     * can see from the given square, with blockers.
     * @param square The square the bishop is on
     * @param blockers Blockers bitboard
     * @return bishop attack bitboard
     */
    private long generateBishopAttacksWithBlockers(int square, long blockers) {
        long attacks = 0L;
        int tr = square / 8;
        int tf = square % 8;

        int r, f;
        for (r = tr + 1, f = tf + 1; r <= 7 && f <= 7; r++, f++) {
            attacks |= (1L << (r * 8 + f));
            if (((1L << (r * 8 + f)) & blockers) != 0) {
                break;
            }
        }
        for (r = tr - 1, f = tf + 1; r >= 0 && f <= 7; r--, f++) {
            attacks |= (1L << (r * 8 + f));
            if (((1L << (r * 8 + f)) & blockers) != 0) {
                break;
            }
        }
        for (r = tr + 1, f = tf - 1; r <= 7 && f >= 0; r++, f--) {
            attacks |= (1L << (r * 8 + f));
            if (((1L << (r * 8 + f)) & blockers) != 0) {
                break;
            }
        }
        for (r = tr - 1, f = tf - 1; r >= 0 && f >= 0; r--, f--) {
            attacks |= (1L << (r * 8 + f));
            if (((1L << (r * 8 + f)) & blockers) != 0) {
                break;
            }
        }

        return attacks;
    }

    /**
     * This function is used to calculate all the squares that the rook
     * can see from the given square, ignoring blockers.
     * @param square The square the rook is on
     * @return rook attack bitboard
     */
    private long generateRookAttacks(int square) {
        long attacks = 0L;
        int tr = square / 8;
        int tf = square % 8;

        int r, f;
        for (r = tr + 1; r <= 6; r++) {
            attacks |= (1L << (r * 8 + tf));
        }
        for (r = tr - 1; r >= 1; r--) {
            attacks |= (1L << (r * 8 + tf));
        }
        for (f = tf + 1; f <= 6; f++) {
            attacks |= (1L << (tr * 8 + f));
        }
        for (f = tf - 1; f >= 1; f--) {
            attacks |= (1L << (tr * 8 + f));
        }

        return attacks;
    }

    /**
     * This function is used to calculate all the squares that the rook
     * can see from the given square, with blockers.
     * @param square The square the rook is on
     * @param blockers The bitboard of blocking pieces
     * @return rook attack bitboard
     */
    private long generateRookAttacksWithBlockers(int square, long blockers) {
        long attacks = 0L;
        int tr = square / 8;
        int tf = square % 8;

        int r, f;
        for (r = tr + 1; r <= 7; r++) {
            attacks |= (1L << (r * 8 + tf));
            if (((1L << (r * 8 + tf)) & blockers) != 0) {
                break;
            }
        }
        for (r = tr - 1; r >= 0; r--) {
            attacks |= (1L << (r * 8 + tf));
            if (((1L << (r * 8 + tf)) & blockers) != 0) {
                break;
            }
        }
        for (f = tf + 1; f <= 7; f++) {
            attacks |= (1L << (tr * 8 + f));
            if (((1L << (tr * 8 + f)) & blockers) != 0) {
                break;
            }
        }
        for (f = tf - 1; f >= 0; f--) {
            attacks |= (1L << (tr * 8 + f));
            if (((1L << (tr * 8 + f)) & blockers) != 0) {
                break;
            }
        }

        return attacks;
    }

    /**
     * This function is used to calculate all the squares that the king
     * is attacking from the given square.
     * @param square The square the king is on
     * @return king attack bitboard
     */
    private long generateKingAttacks(int square) {
        long attacks = 0L;
        long bitboard = 1L << square;

        if ((bitboard >>> 8) != 0) attacks |= (bitboard >>> 8);
        if (((bitboard >>> 9) & NOT_H_FILE_MASK) != 0) attacks |= (bitboard >>> 9);
        if (((bitboard >>> 7) & NOT_A_FILE_MASK) != 0) attacks |= (bitboard >>> 7);
        if (((bitboard >>> 1) & NOT_H_FILE_MASK) != 0) attacks |= (bitboard >>> 1);
        if ((bitboard << 8) != 0) attacks |= (bitboard << 8);
        if (((bitboard << 9) & NOT_A_FILE_MASK) != 0) attacks |= (bitboard << 9);
        if (((bitboard << 7) & NOT_H_FILE_MASK) != 0) attacks |= (bitboard << 7);
        if (((bitboard << 1) & NOT_A_FILE_MASK) != 0) attacks |= (bitboard << 1);

        return attacks;
    }

    /**
     * This function is used to get an occupancy bitboard for the given attack mask.
     * If a bit is set in the attack mask, this bit will either be on or off in our occupancy mask.
     * This is because this function will be called for every number from 0 to 2^(max bit count of the attack mask).
     * For every such bitmask, we call this function, and then we pop the bits from the attack mask and
     * check if it should be present in the index mask by & operator, and if it is we put it in the mask.
     * @param index The mask used to check which bits are needed in the occupancy mask
     * @param attackMask The mask of the squares attacked by the piece
     * @return
     */
    private long getOccupancy(int index, long attackMask) {
        long occupancy = 0L;
        int bitCount = Long.bitCount(attackMask);
        for (int i = 0; i < bitCount; i++) {
            int square = getLs1bIndex(attackMask);
            attackMask = popBit(attackMask, square);
            if ((index & (1 << i)) != 0) {
                occupancy |= (1L << square);
            }
        }
        return occupancy;
    }

    public void logBitboard(long bitboard) {
        StringBuilder sb = new StringBuilder("\n\n");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (j == 0) sb.append("   ").append(8 - i).append("   ");
                sb.append(getBit(bitboard, 8 * i + j) ? 1 : 0).append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n       a b c d e f g h\n");
        EngineLogger.debug(sb.toString());
    }

    private boolean getBit(long bitboard, int square) {
        return (bitboard & (1L << square)) != 0;
    }

    private long setBit(long bitboard, int square) {
        return bitboard | (1L << square);
    }

    private long popBit(long bitboard, int square) {
        return getBit(bitboard, square) ? bitboard ^ (1L << square) : bitboard;
    }

    private int getLs1bIndex(long bitboard) {
        if (bitboard != 0) {
            return Long.bitCount((bitboard & -bitboard) - 1);
        }
        return -1;
    }

    // Convert algebraic notation (e.g., "e4") to bitboard index (0..63)
    public static int algToIdx(String algebraic) {
        char file = algebraic.charAt(0);
        char rank = algebraic.charAt(1);
        int fileIndex = file - 'a';
        int rankIndex = rank - '1';
        return rankIndex * 8 + fileIndex;
    }

    // Convert bitboard index (0..63) to algebraic notation (e.g., "e4")
    public static String idxToAlg(int index) {
        int fileIndex = index % 8;
        int rankIndex = index / 8;
        char file = (char) ('a' + fileIndex);
        char rank = (char) ('1' + rankIndex);
        return "" + file + rank;
    }
}
