package engine.util;

import app.Constants;
import engine.state.Bitboard;
import engine.core.bitboard.BitboardHelper;

import java.util.ArrayList;
import java.util.List;

import static app.Constants.*;

public class BitboardMoveGenerator {
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
    private Bitboard bitboard;
    private BitboardHelper bitboardHelper;
    private boolean isWhiteTurn = true;
    List<Integer> moves = new ArrayList<>();

    public BitboardMoveGenerator(BitboardHelper bitboardHelper) {
        this.bitboardHelper = bitboardHelper;
    }

    public void setBitboard(Bitboard bitboard) {
        this.bitboard = bitboard;
        logAttacks();
    }

    public List<Integer> generateLegalMoves(boolean isWhiteTurn) {
        this.isWhiteTurn = isWhiteTurn;
        generateSinglePawnPushMoves();
        generateDoublePawnPushMoves();
        generateCapturePawnMoves();
        generateKnightMoves();
        generateKingMoves();
        generateBishopMoves();
        generateRookMoves();
        generateQueenMoves();
        return null;
    }

    public void generateSinglePawnPushMoves() {
        if (isWhiteTurn) {
            long whitePawnTargets = (bitboard.getPieces()[pieceMap.get('P')] << 8) & ~bitboard.getOccupancies()[BOTH];
            while (whitePawnTargets != 0) {
                int toSquare = BitUtils.getLs1bIndex(whitePawnTargets);
                int fromSquare = toSquare + 8;
                // TODO add move to move list
                if (toSquare < 8) {
                    // TODO add promotion moves to move list
                }
                whitePawnTargets = BitUtils.popBit(whitePawnTargets, toSquare);
            }
        }
        else {
            long blackPawnTargets = (bitboard.getOccupancies()[pieceMap.get('p')] >>> 8) & ~bitboard.getOccupancies()[BOTH];
            while (blackPawnTargets != 0) {
                int toSquare = BitUtils.getLs1bIndex(blackPawnTargets);
                int fromSquare = toSquare - 8;
                // TODO add move to move list
                if (toSquare >= 56) {
                    // TODO add promotion moves to move list
                }
                blackPawnTargets = BitUtils.popBit(blackPawnTargets, toSquare);
            }
        }
    }

    public void generateDoublePawnPushMoves() {
        if (isWhiteTurn) {
            long whiteSinglePushTargets = (bitboard.getPieces()[pieceMap.get('P')] << 8) & ~bitboard.getOccupancies()[BOTH];
            long whiteDoublePushTargets = (whiteSinglePushTargets << 8) & ~bitboard.getOccupancies()[BOTH] & RANK_4;
            while (whiteDoublePushTargets != 0) {
                int toSquare = BitUtils.getLs1bIndex(whiteDoublePushTargets);
                int fromSquare = toSquare + 16;
                // TODO add move to move list
                whiteDoublePushTargets = BitUtils.popBit(whiteDoublePushTargets, toSquare);
            }
        }
        else {
            long blackSinglePushTargets = (bitboard.getOccupancies()[pieceMap.get('p')] >>> 8) & ~bitboard.getOccupancies()[BOTH];
            long blackDoublePushTargets = (blackSinglePushTargets >>> 8) & ~bitboard.getOccupancies()[BOTH] & RANK_5;
            while (blackDoublePushTargets != 0) {
                int toSquare = BitUtils.getLs1bIndex(blackDoublePushTargets);
                int fromSquare = toSquare - 16;
                // TODO add move to move list
                blackDoublePushTargets = BitUtils.popBit(blackDoublePushTargets, toSquare);
            }
        }
    }

    public void generateCapturePawnMoves() {
        if (isWhiteTurn) {
            long whitePawns = bitboard.getPieces()[pieceMap.get('P')];
            while (whitePawns != 0) {
                int fromSquare = BitUtils.getLs1bIndex(whitePawns);
                long toSquares = bitboardHelper.pawnAttacks[WHITE][fromSquare] & bitboard.getOccupancies()[BLACK];
                if (bitboard.getEnPassantSquare() != -1
                        && (bitboardHelper.pawnAttacks[WHITE][fromSquare] & (1L << bitboard.getEnPassantSquare())) != 0) {
                    // TODO add en passant move to move list
                }
                // TODO add moves to move list
                if (fromSquare >= 8 && fromSquare < 16) {
                    // TODO add promotion moves to move list
                }
                whitePawns = BitUtils.popBit(whitePawns, fromSquare);
            }
        }
        else {
            long blackPawns = bitboard.getPieces()[pieceMap.get('p')];
            while (blackPawns != 0) {
                int fromSquare = BitUtils.getLs1bIndex(blackPawns);
                long toSquares = bitboardHelper.pawnAttacks[BLACK][fromSquare] & bitboard.getOccupancies()[WHITE];
                if (bitboard.getEnPassantSquare() != -1
                        && (bitboardHelper.pawnAttacks[BLACK][fromSquare] & (1L << bitboard.getEnPassantSquare())) != 0) {
                    // TODO add en passant move to move list
                }
                // TODO add moves to move list
                if (fromSquare >= 48 && fromSquare < 56) {
                    // TODO add promotion moves to move list
                }
                blackPawns = BitUtils.popBit(blackPawns, fromSquare);
            }
        }
    }

    public void generateKnightMoves() {
        long knights = isWhiteTurn ? bitboard.getPieces()[pieceMap.get('N')] : bitboard.getPieces()[pieceMap.get('n')];
        while (knights != 0) {
            int fromSquare = BitUtils.getLs1bIndex(knights);
            long toSquares = bitboardHelper.knightAttacks[fromSquare]
                    & (isWhiteTurn ? ~bitboard.getOccupancies()[WHITE] : ~bitboard.getOccupancies()[BLACK]);
            // TODO add moves to move list
            knights = BitUtils.popBit(knights, fromSquare);
        }
    }

    public void generateKingMoves() {
        long king = isWhiteTurn ? bitboard.getPieces()[pieceMap.get('K')] : bitboard.getPieces()[pieceMap.get('k')];
        int fromSquare = BitUtils.getLs1bIndex(king);
        long toSquares = bitboardHelper.kingAttacks[fromSquare]
                & (isWhiteTurn ? ~bitboard.getOccupancies()[WHITE] : ~bitboard.getOccupancies()[BLACK]);
        // TODO add moves to move list

        if (isWhiteTurn) {
            if ((bitboard.getCastlesFlags() & WHITE_KINGSIDE_CASTLES_MASK) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_KINGSIDE_ROOK - 1))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_KINGSIDE_ROOK - 2))) != 0
                    && !isSquareAttacked(WHITE_KINGSIDE_ROOK - 1, false)
                    && !isSquareAttacked(WHITE_KINGSIDE_ROOK - 2, false)) {
                // TODO add castles move to list
            }
            if ((bitboard.getCastlesFlags() & WHITE_QUEENSIDE_CASTLES_MASK) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_QUEENSIDE_ROOK + 1))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_QUEENSIDE_ROOK + 2))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_QUEENSIDE_ROOK + 3))) != 0
                    && !isSquareAttacked(WHITE_QUEENSIDE_ROOK + 2, false)
                    && !isSquareAttacked(WHITE_QUEENSIDE_ROOK + 3, false)) {
                // TODO add castles move to list
            }
        }
        else {
            if ((bitboard.getCastlesFlags() & BLACK_KINGSIDE_CASTLES_MASK) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_KINGSIDE_ROOK - 1))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_KINGSIDE_ROOK - 2))) != 0
                    && !isSquareAttacked(BLACK_KINGSIDE_ROOK - 1, true)
                    && !isSquareAttacked(BLACK_KINGSIDE_ROOK - 2, true)) {
                // TODO add castles move to list
            }
            if ((bitboard.getCastlesFlags() & BLACK_QUEENSIDE_CASTLES_MASK) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_QUEENSIDE_ROOK + 1))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_QUEENSIDE_ROOK + 2))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_QUEENSIDE_ROOK + 3))) != 0
                    && !isSquareAttacked(BLACK_QUEENSIDE_ROOK + 2, false)
                    && !isSquareAttacked(BLACK_QUEENSIDE_ROOK + 3, false)) {
                // TODO add castles move to list
            }
        }
    }

    public void generateBishopMoves() {
        long bishops = isWhiteTurn ? bitboard.getPieces()[pieceMap.get('B')] : bitboard.getPieces()[pieceMap.get('b')];
        while (bishops != 0) {
            int fromSquare = BitUtils.getLs1bIndex(bishops);
            long toSquares = bitboardHelper.generateBishopAttacksWithMagics(fromSquare, bitboard.getOccupancies()[BOTH])
                    & (isWhiteTurn ? ~bitboard.getOccupancies()[WHITE] : ~bitboard.getOccupancies()[BLACK]);
            // TODO add moves to move list
            bishops = BitUtils.popBit(bishops, fromSquare);
        }
    }

    public void generateRookMoves() {
        long rooks = isWhiteTurn ? bitboard.getPieces()[pieceMap.get('R')] : bitboard.getPieces()[pieceMap.get('r')];
        while (rooks != 0) {
            int fromSquare = BitUtils.getLs1bIndex(rooks);
            long toSquares = bitboardHelper.generateRookAttacksWithMagics(fromSquare, bitboard.getOccupancies()[BOTH])
                    & (isWhiteTurn ? ~bitboard.getOccupancies()[WHITE] : ~bitboard.getOccupancies()[BLACK]);
            // TODO add moves to move list
            rooks = BitUtils.popBit(rooks, fromSquare);
        }
    }

    public void generateQueenMoves() {
        long queens = isWhiteTurn ? bitboard.getPieces()[pieceMap.get('Q')] : bitboard.getPieces()[pieceMap.get('q')];
        while (queens != 0) {
            int fromSquare = BitUtils.getLs1bIndex(queens);
            long toSquares = bitboardHelper.generateQueenAttacksWithMagics(fromSquare, bitboard.getOccupancies()[BOTH])
                    & (isWhiteTurn ? ~bitboard.getOccupancies()[WHITE] : ~bitboard.getOccupancies()[BLACK]);
            // TODO add moves to move list
            queens = BitUtils.popBit(queens, fromSquare);
        }
    }

    private boolean isSquareAttacked(int square, boolean isWhiteTurn) {
        if (!isWhiteTurn) {
            // This square is attacked by a white pawn if there is a white pawn on any of the squares that a black pawn would be attacking from this square
            if ((bitboardHelper.pawnAttacks[WHITE][square] & bitboard.getPieces()[Constants.pieceMap.get('p')]) != 0) {
                return true;
            }
            if ((bitboardHelper.knightAttacks[square] & bitboard.getPieces()[Constants.pieceMap.get('n')]) != 0) {
                return true;
            }
            if ((bitboardHelper.kingAttacks[square] & bitboard.getPieces()[Constants.pieceMap.get('k')]) != 0) {
                return true;
            }
            if ((bitboardHelper.generateBishopAttacksWithMagics(square, bitboard.getOccupancies()[BOTH]) & bitboard.getPieces()[Constants.pieceMap.get('b')]) != 0) {
                return true;
            }
            if ((bitboardHelper.generateRookAttacksWithMagics(square, bitboard.getOccupancies()[BOTH]) & bitboard.getPieces()[Constants.pieceMap.get('r')]) != 0) {
                return true;
            }
            if ((bitboardHelper.generateQueenAttacksWithMagics(square, bitboard.getOccupancies()[BOTH]) & bitboard.getPieces()[Constants.pieceMap.get('q')]) != 0) {
                return true;
            }
        }
        else {
            // This square is attacked by a black pawn if there is a black pawn on any of the squares that a white pawn would be attacking from this square
            if ((bitboardHelper.pawnAttacks[BLACK][square] & bitboard.getPieces()[Constants.pieceMap.get('P')]) != 0) {
                return true;
            }
            if ((bitboardHelper.knightAttacks[square] & bitboard.getPieces()[Constants.pieceMap.get('N')]) != 0) {
                return true;
            }
            if ((bitboardHelper.kingAttacks[square] & bitboard.getPieces()[Constants.pieceMap.get('K')]) != 0) {
                return true;
            }
            if ((bitboardHelper.generateBishopAttacksWithMagics(square, bitboard.getOccupancies()[BOTH]) & bitboard.getPieces()[Constants.pieceMap.get('B')]) != 0) {
                return true;
            }
            if ((bitboardHelper.generateRookAttacksWithMagics(square, bitboard.getOccupancies()[BOTH]) & bitboard.getPieces()[Constants.pieceMap.get('R')]) != 0) {
                return true;
            }
            if ((bitboardHelper.generateQueenAttacksWithMagics(square, bitboard.getOccupancies()[BOTH]) & bitboard.getPieces()[Constants.pieceMap.get('Q')]) != 0) {
                return true;
            }
        }
        return false;
    }

    private void logAttacks() {
        long bitboard = 0L;
        for (int square = 0; square < BOARD_SIZE; square++) {
            if (isSquareAttacked(square, isWhiteTurn)) bitboard |= (1L << square);
        }
        BitUtils.logBitboard(bitboard);
    }
}
