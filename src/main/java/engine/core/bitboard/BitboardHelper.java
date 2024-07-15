package engine.core.bitboard;

import app.Constants;
import app.EngineLogger;
import engine.core.entity.Piece;
import engine.util.bits.BitUtils;

import java.util.Random;

import static app.Constants.BLACK;
import static app.Constants.WHITE;

public class BitboardHelper {
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

    private final int BOARD_SIZE = Constants.BOARD_SIZE;

    /**
     * The maximum number of pieces that can block a sliding piece's attack
     * (6 in both directions for rooks and bishops)
     */
    private final int MAXIMUM_BLOCKING_PIECES = 12;

    // TODO separate blocking mask number for bishops and rooks
    private final int MAXIMUM_BLOCKING_PIECES_MASK = 1 << MAXIMUM_BLOCKING_PIECES;

    public final long[][] pawnAttacks = new long[2][BOARD_SIZE];
    public final long[] knightAttacks = new long[BOARD_SIZE];
    public final long[] bishopAttacks = new long[BOARD_SIZE];
    public final long[] rookAttacks = new long[BOARD_SIZE];
    public final long[] kingAttacks = new long[BOARD_SIZE];

    public final long[][] bishopOccupancies = new long[BOARD_SIZE][MAXIMUM_BLOCKING_PIECES_MASK];
    public final long[][] rookOccupancies = new long[BOARD_SIZE][MAXIMUM_BLOCKING_PIECES_MASK];
    public final long[][] bishopAttacksWithBlockers = new long[BOARD_SIZE][MAXIMUM_BLOCKING_PIECES_MASK];
    public final long[][] rookAttacksWithBlockers = new long[BOARD_SIZE][MAXIMUM_BLOCKING_PIECES_MASK];
    public final int[] bishopRelevantBits = new int[BOARD_SIZE];
    public final int[] rookRelevantBits = new int[BOARD_SIZE];

    public record MagicRecord(long magicNumber, long[] attackMap) {}
    public final MagicRecord[] bishopMagics = new MagicRecord[BOARD_SIZE];
    public final MagicRecord[] rookMagics = new MagicRecord[BOARD_SIZE];

    public BitboardHelper() {
        fillAttackTables();
    }

    /**
     * The main function in this class.
     * Used to fill in all the attack tables for every kind of piece on the board.
     * Here we either generate magic constants or reuse the generated ones,
     * which are hardcoded in the MagicConstants class.
     */
    private void fillAttackTables() {
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
            bishopRelevantBits[i] = Long.bitCount(bishopAttacks[i]);
            rookRelevantBits[i] = Long.bitCount(rookAttacks[i]);
        }
        // The generated magic numbers are hardcoded
        //generateMagics();

        // Fill in magic containers from hardcoded magic constants
        initializeMagicRecords();
    }


    private int magicHash(int square, long relevantOccupancy, int relevantBits, byte piece) {
        if (piece == Piece.BISHOP) {
            return (int)((relevantOccupancy * MagicConstants.bishopMagics[square]) >>> (BOARD_SIZE - relevantBits));
        }
        else { // ROOK
            return (int)((relevantOccupancy * MagicConstants.rookMagics[square]) >>> (BOARD_SIZE - relevantBits));
        }
    }

    /**
     * This function is used to read the magic constants from the MagicConstants class,
     * and to fill in attack maps for bishops and rooks using the said constants.
     */
    private void initializeMagicRecords() {
        for (int square = 0; square < BOARD_SIZE; square++) {
            long[] attackMap = new long[MAXIMUM_BLOCKING_PIECES_MASK];
            int relevantBits = Long.bitCount(bishopAttacks[square]);
            for (int i = 0; i < MAXIMUM_BLOCKING_PIECES_MASK; i++) {
                int magicKey = magicHash(square, bishopOccupancies[square][i], relevantBits, Piece.BISHOP);
                attackMap[magicKey] = bishopAttacksWithBlockers[square][i];
            }
            bishopMagics[square] = new MagicRecord(MagicConstants.bishopMagics[square], attackMap);
        }

        for (int square = 0; square < BOARD_SIZE; square++) {
            long[] attackMap = new long[MAXIMUM_BLOCKING_PIECES_MASK];
            int relevantBits = Long.bitCount(rookAttacks[square]);
            for (int i = 0; i < MAXIMUM_BLOCKING_PIECES_MASK; i++) {
                int magicKey = magicHash(square, rookOccupancies[square][i], relevantBits, Piece.ROOK);
                attackMap[magicKey] = rookAttacksWithBlockers[square][i];
            }
            rookMagics[square] = new MagicRecord(MagicConstants.rookMagics[square], attackMap);
        }
//        System.out.println("Bishop magics");
//        for (int square = 0; square < 64; square++) {
//            System.out.println(bishopMagics[square].getMagicNumber()+"L,");
//        }
//        System.out.println("\nRook magics");
//        for (int square = 0; square < 64; square++) {
//            System.out.println(rookMagics[square].getMagicNumber()+"L,");
//        }
    }

    /**
     * This function is used to generate magic constants for rooks and bishops.
     * We should only call this function if we want to regenerate the magic constants,
     * as they can be hardcoded and reused and generating new ones takes a bit of time.
     */
    private void generateMagics() {
        for (int square = 0; square < BOARD_SIZE; square++) {
            bishopMagics[square] = generateBishopMagic(square);
            rookMagics[square] = generateRookMagic(square);
        }
        System.out.println("Bishop magics");
        for (int square = 0; square < 64; square++) {
            System.out.println(bishopMagics[square].magicNumber()+"L,");
        }
        System.out.println("\nRook magics");
        for (int square = 0; square < 64; square++) {
            System.out.println(rookMagics[square].magicNumber()+"L,");
        }
    }

    /**
     * This function is used to generate a magic constant for a bishop on a given square.
     * @param square The square that the bishop is on
     * @return The class containing the magic constant
     */
    public MagicRecord generateBishopMagic(int square) {
        Random random = new Random();
        int relevantBits = Long.bitCount(bishopAttacks[square]);

        for (int iterations = 0; iterations < 1e8; iterations++) {
            long magicCandidate = random.nextLong() & random.nextLong() & random.nextLong();
            long[] attackMap = new long[MAXIMUM_BLOCKING_PIECES_MASK];
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
                return new MagicRecord(magicCandidate, attackMap);
            }
        }
        EngineLogger.error("Could not find magic number.");
        return null;
    }

    /**
     * This function is used to generate a magic constant for a rook on a given square.
     * @param square The square that the rook is on
     * @return The class containing the magic constant
     */
    private MagicRecord generateRookMagic(int square) {
        Random random = new Random();
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
                return new MagicRecord(magicCandidate, attackMap);
            }
        }
        EngineLogger.error("Could not find magic number.");
        return null;
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
     * This function is used to create an attack map of a bishop
     * on a given square with the given occupancy bitboard.
     * In contrast with the same functions above, it can generate the attack bitboard
     * in practically O(1) time using precalculated magic hash maps.
     * @param square The position of the bishop
     * @param occupancy The occupancy bitboard
     * @return The bishop attack bitboard
     */
    public long generateBishopAttacksWithMagics(int square, long occupancy) {
        int relevantBits = bishopRelevantBits[square];
        long relevantOccupancy = occupancy & bishopAttacks[square];
        int magicKey = magicHash(square, relevantOccupancy, relevantBits, Piece.BISHOP);
        return bishopMagics[square].attackMap()[magicKey];
    }

    /**
     * This function is used to create an attack map of a rook
     * on a given square with the given occupancy bitboard.
     * In contrast with the same functions above, it can generate the attack bitboard
     * in practically O(1) time using precalculated magic hash maps.
     * @param square The position of the rook
     * @param occupancy The occupancy bitboard
     * @return The rook attack bitboard
     */
    public long generateRookAttacksWithMagics(int square, long occupancy) {
        int relevantBits = rookRelevantBits[square];
        long relevantOccupancy = occupancy & rookAttacks[square];
        int magicKey = magicHash(square, relevantOccupancy, relevantBits, Piece.ROOK);
        return rookMagics[square].attackMap()[magicKey];
    }

    /**
     * This function is used to create an attack map of a queen
     * on a given square with the given occupancy bitboard.
     * @param square The position of the queen
     * @param occupancy The occupancy bitboard
     * @return The queen attack bitboard (bishop attack map | rook attack map)
     */
    public long generateQueenAttacksWithMagics(int square, long occupancy) {
        long bishopRelevantOccupancy = occupancy & bishopAttacks[square];
        int bishopMagicKey = magicHash(square, bishopRelevantOccupancy, bishopRelevantBits[square], Piece.BISHOP);

        long rookRelevantOccupancy = occupancy & rookAttacks[square];
        int rookMagicKey = magicHash(square, rookRelevantOccupancy, rookRelevantBits[square], Piece.ROOK);

        return bishopMagics[square].attackMap()[bishopMagicKey] | rookMagics[square].attackMap()[rookMagicKey];
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
            int square = BitUtils.getLs1bIndex(attackMask);
            attackMask = BitUtils.popBit(attackMask, square);
            if ((index & (1 << i)) != 0) {
                occupancy |= (1L << square);
            }
        }
        return occupancy;
    }
}
