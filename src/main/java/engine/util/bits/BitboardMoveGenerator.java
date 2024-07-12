package engine.util.bits;

import engine.core.bitboard.BitboardHelper;
import engine.core.state.Bitboard;

import static app.Constants.pieceMap;
import static app.Constants.*;
import static engine.core.entity.Piece.*;

public class BitboardMoveGenerator {
    private Bitboard bitboard;
    private BitboardHelper bitboardHelper;
    private boolean isWhiteTurn = true;
    private int[] moves = new int[MAX_LEGAL_MOVES];
    private int moveCounter = 0;

    public BitboardMoveGenerator(BitboardHelper bitboardHelper) {
        this.bitboardHelper = bitboardHelper;
    }

    public void setBitboard(Bitboard bitboard) {
        this.bitboard = bitboard;
    }

    public int getMoveCounter() {
        return moveCounter;
    }

    public void clearMoves() {
        moveCounter = 0;
    }

    public int[] generateLegalMoves(boolean isWhiteTurn) {
        this.isWhiteTurn = isWhiteTurn;
        generateSinglePawnPushMoves();
        generateDoublePawnPushMoves();
        generatePawnCaptureMoves();
        generateKnightMoves();
        generateKingMoves();
        generateBishopMoves();
        generateRookMoves();
        generateQueenMoves();
        return moves;
    }

    public void generateSinglePawnPushMoves() {
        int move;
        if (isWhiteTurn) {
            long whitePawnTargets = (bitboard.getPieces()[pieceMap.get('P')] >>> 8) & ~bitboard.getOccupancies()[BOTH];
            while (whitePawnTargets != 0) {
                int toSquare = BitUtils.getLs1bIndex(whitePawnTargets);
                int fromSquare = toSquare + 8;

                if (toSquare > RANK_8_END_SQUARE) {
                    move = MoveEncoder.encodeMove(fromSquare, toSquare, WhitePawn,
                            0, 0, 0, 0, 0);
                    addMove(move);
                }

                else {
                    move = MoveEncoder.encodeMove(fromSquare, toSquare, WhitePawn,
                            WhiteQueen, 0, 0, 0, 0);
                    addMove(move);

                    move = MoveEncoder.encodeMove(fromSquare, toSquare, WhitePawn,
                            WhiteRook, 0,0, 0, 0);
                    addMove(move);

                    move = MoveEncoder.encodeMove(fromSquare, toSquare, WhitePawn,
                            WhiteBishop, 0, 0, 0, 0);
                    addMove(move);

                    move = MoveEncoder.encodeMove(fromSquare, toSquare, WhitePawn,
                            WhiteKnight, 0, 0, 0, 0);
                    addMove(move);
                }
                whitePawnTargets = BitUtils.popBit(whitePawnTargets, toSquare);
            }
        }
        else {
            long blackPawnTargets = (bitboard.getPieces()[pieceMap.get('p')] << 8) & ~bitboard.getOccupancies()[BOTH];
            while (blackPawnTargets != 0) {
                int toSquare = BitUtils.getLs1bIndex(blackPawnTargets);
                int fromSquare = toSquare - 8;

                if (toSquare < RANK_1_START_SQUARE) {
                    move = MoveEncoder.encodeMove(fromSquare, toSquare, BlackPawn,
                            0, 0, 0, 0, 0);
                    addMove(move);
                }

                else {
                    move = MoveEncoder.encodeMove(fromSquare, toSquare, BlackPawn,
                            BlackQueen, 0, 0, 0, 0);
                    addMove(move);

                    move = MoveEncoder.encodeMove(fromSquare, toSquare, BlackPawn,
                            BlackRook, 0,0, 0, 0);
                    addMove(move);

                    move = MoveEncoder.encodeMove(fromSquare, toSquare, BlackPawn,
                            BlackBishop, 0, 0, 0, 0);
                    addMove(move);

                    move = MoveEncoder.encodeMove(fromSquare, toSquare, BlackPawn,
                            BlackKnight, 0, 0, 0, 0);
                    addMove(move);
                }
                blackPawnTargets = BitUtils.popBit(blackPawnTargets, toSquare);
            }
        }
    }

    public void generateDoublePawnPushMoves() {
        int move;
        if (isWhiteTurn) {
            long whiteSinglePushTargets = (bitboard.getPieces()[pieceMap.get('P')] >>> 8) & ~bitboard.getOccupancies()[BOTH];
            long whiteDoublePushTargets = (whiteSinglePushTargets >>> 8) & ~bitboard.getOccupancies()[BOTH] & RANK_4;
            while (whiteDoublePushTargets != 0) {
                int toSquare = BitUtils.getLs1bIndex(whiteDoublePushTargets);
                int fromSquare = toSquare + 16;

                move = MoveEncoder.encodeMove(fromSquare, toSquare, WhitePawn,
                        0, 1, 0, 0, 0);
                addMove(move);

                whiteDoublePushTargets = BitUtils.popBit(whiteDoublePushTargets, toSquare);
            }
        }
        else {
            long blackSinglePushTargets = (bitboard.getPieces()[pieceMap.get('p')] << 8) & ~bitboard.getOccupancies()[BOTH];
            long blackDoublePushTargets = (blackSinglePushTargets << 8) & ~bitboard.getOccupancies()[BOTH] & RANK_5;
            while (blackDoublePushTargets != 0) {
                int toSquare = BitUtils.getLs1bIndex(blackDoublePushTargets);
                int fromSquare = toSquare - 16;

                move = MoveEncoder.encodeMove(fromSquare, toSquare, BlackPawn,
                        0, 1, 0, 0, 0);
                addMove(move);

                blackDoublePushTargets = BitUtils.popBit(blackDoublePushTargets, toSquare);
            }
        }
    }

    public void generatePawnCaptureMoves() {
        int move;
        if (isWhiteTurn) {
            long whitePawns = bitboard.getPieces()[pieceMap.get('P')];
            while (whitePawns != 0) {
                int fromSquare = BitUtils.getLs1bIndex(whitePawns);
                long toSquares = bitboardHelper.pawnAttacks[WHITE][fromSquare] & bitboard.getOccupancies()[BLACK];

                if (bitboard.getEnPassantSquare() != -1
                        && (bitboardHelper.pawnAttacks[WHITE][fromSquare] & (1L << (bitboard.getEnPassantSquare() - 8))) != 0) {
                    move = MoveEncoder.encodeMove(fromSquare, bitboard.getEnPassantSquare() - 8, WhitePawn,
                            0, 0, 0, 1, 1);
                    addMove(move);
                }

                if (fromSquare >= RANK_7_START_SQUARE && fromSquare <= RANK_7_END_SQUARE) {
                    while (toSquares != 0) {
                        int toSquare = BitUtils.getLs1bIndex(toSquares);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, WhitePawn,
                                WhiteQueen, 0, 0, 0, 1);
                        addMove(move);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, WhitePawn,
                                WhiteRook, 0, 0, 0, 1);
                        addMove(move);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, WhitePawn,
                                WhiteBishop, 0, 0, 0, 1);
                        addMove(move);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, WhitePawn,
                                WhiteKnight, 0, 0, 0, 1);
                        addMove(move);

                        toSquares = BitUtils.popBit(toSquares, toSquare);
                    }
                }

                else {
                    while (toSquares != 0) {
                        int toSquare = BitUtils.getLs1bIndex(toSquares);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, WhitePawn,
                                0, 0, 0, 0, 1);
                        addMove(move);

                        toSquares = BitUtils.popBit(toSquares, toSquare);
                    }
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
                        && (bitboardHelper.pawnAttacks[BLACK][fromSquare] & (1L << (bitboard.getEnPassantSquare() + 8))) != 0) {
                    move = MoveEncoder.encodeMove(fromSquare, bitboard.getEnPassantSquare() + 8, BlackPawn,
                            0, 0, 0, 1, 1);
                    addMove(move);
                }

                if (fromSquare >= RANK_2_START_SQUARE && fromSquare <= RANK_2_END_SQUARE) {
                    while (toSquares != 0) {
                        int toSquare = BitUtils.getLs1bIndex(toSquares);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, BlackPawn,
                                BlackQueen, 0, 0, 0, 1);
                        addMove(move);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, BlackPawn,
                                BlackRook, 0, 0, 0, 1);
                        addMove(move);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, BlackPawn,
                                BlackBishop, 0, 0, 0, 1);
                        addMove(move);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, BlackPawn,
                                BlackKnight, 0, 0, 0, 1);
                        addMove(move);

                        toSquares = BitUtils.popBit(toSquares, toSquare);
                    }
                }

                else {
                    while (toSquares != 0) {
                        int toSquare = BitUtils.getLs1bIndex(toSquares);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, BlackPawn,
                                0, 0, 0, 0, 1);
                        addMove(move);

                        toSquares = BitUtils.popBit(toSquares, toSquare);
                    }
                }

                blackPawns = BitUtils.popBit(blackPawns, fromSquare);
            }
        }
    }

    public void generateKnightMoves() {
        int move;
        long knights = isWhiteTurn ? bitboard.getPieces()[pieceMap.get('N')] : bitboard.getPieces()[pieceMap.get('n')];
        while (knights != 0) {
            int fromSquare = BitUtils.getLs1bIndex(knights);
            long toSquares = bitboardHelper.knightAttacks[fromSquare]
                    & (isWhiteTurn ? ~bitboard.getOccupancies()[WHITE] : ~bitboard.getOccupancies()[BLACK]);

            while (toSquares != 0) {
                int toSquare = BitUtils.getLs1bIndex(toSquares);
                boolean isCapture = isWhiteTurn ?
                        (bitboard.getOccupancies()[BLACK] & (1L << toSquare)) != 0 : (bitboard.getOccupancies()[WHITE] & (1L << toSquare)) != 0;

                move = MoveEncoder.encodeMove(fromSquare, toSquare, isWhiteTurn ? WhiteKnight : BlackKnight,
                        0, 0, 0, 0, isCapture ? 1 : 0);
                addMove(move);

                toSquares = BitUtils.popBit(toSquares, toSquare);
            }
            knights = BitUtils.popBit(knights, fromSquare);
        }
    }

    public void generateKingMoves() {
        int move;
        long king = isWhiteTurn ? bitboard.getPieces()[pieceMap.get('K')] : bitboard.getPieces()[pieceMap.get('k')];
        int fromSquare = BitUtils.getLs1bIndex(king);
        long toSquares = bitboardHelper.kingAttacks[fromSquare]
                & (isWhiteTurn ? ~bitboard.getOccupancies()[WHITE] : ~bitboard.getOccupancies()[BLACK]);

        while (toSquares != 0) {
            int toSquare = BitUtils.getLs1bIndex(toSquares);
            boolean isCapture = isWhiteTurn ?
                    (bitboard.getOccupancies()[BLACK] & (1L << toSquare)) != 0 : (bitboard.getOccupancies()[WHITE] & (1L << toSquare)) != 0;

            move = MoveEncoder.encodeMove(fromSquare, toSquare, isWhiteTurn ? WhiteKing : BlackKing,
                    0, 0, 0, 0, isCapture ? 1 : 0);
            addMove(move);

            toSquares = BitUtils.popBit(toSquares, toSquare);
        }

        if (isWhiteTurn) {
            if ((bitboard.getCastlesFlags() & WHITE_KINGSIDE_CASTLES_MASK) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_KINGSIDE_ROOK - 1))) == 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_KINGSIDE_ROOK - 2))) == 0
                    && !isSquareAttacked(WHITE_KINGSIDE_ROOK - 1, false)
                    && !isSquareAttacked(WHITE_KINGSIDE_ROOK - 2, false)
                    && !isSquareAttacked(WHITE_KINGSIDE_ROOK - 3, false)) {

                move = MoveEncoder.encodeMove(fromSquare, WHITE_KINGSIDE_CASTLES_SQUARE, WhiteKing,
                        0, 0, 1, 0, 0);
                addMove(move);
            }

            if ((bitboard.getCastlesFlags() & WHITE_QUEENSIDE_CASTLES_MASK) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_QUEENSIDE_ROOK + 1))) == 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_QUEENSIDE_ROOK + 2))) == 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_QUEENSIDE_ROOK + 3))) == 0
                    && !isSquareAttacked(WHITE_QUEENSIDE_ROOK + 2, false)
                    && !isSquareAttacked(WHITE_QUEENSIDE_ROOK + 3, false)
                    && !isSquareAttacked(WHITE_QUEENSIDE_ROOK + 4, false)) {

                move = MoveEncoder.encodeMove(fromSquare, WHITE_QUEENSIDE_CASTLES_SQUARE, WhiteKing,
                        0, 0, 1, 0, 0);
                addMove(move);
            }
        }
        else {
            if ((bitboard.getCastlesFlags() & BLACK_KINGSIDE_CASTLES_MASK) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_KINGSIDE_ROOK - 1))) == 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_KINGSIDE_ROOK - 2))) == 0
                    && !isSquareAttacked(BLACK_KINGSIDE_ROOK - 1, true)
                    && !isSquareAttacked(BLACK_KINGSIDE_ROOK - 2, true)
                    && !isSquareAttacked(BLACK_KINGSIDE_ROOK - 3, true)) {

                move = MoveEncoder.encodeMove(fromSquare, BLACK_KINGSIDE_CASTLES_SQUARE, BlackKing,
                        0, 0, 1, 0, 0);
                addMove(move);
            }
            if ((bitboard.getCastlesFlags() & BLACK_QUEENSIDE_CASTLES_MASK) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_QUEENSIDE_ROOK + 1))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_QUEENSIDE_ROOK + 2))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_QUEENSIDE_ROOK + 3))) != 0
                    && !isSquareAttacked(BLACK_QUEENSIDE_ROOK + 2, false)
                    && !isSquareAttacked(BLACK_QUEENSIDE_ROOK + 3, false)
                    && !isSquareAttacked(BLACK_QUEENSIDE_ROOK + 4, false)) {

                move = MoveEncoder.encodeMove(fromSquare, BLACK_QUEENSIDE_CASTLES_SQUARE, BlackKing,
                        0, 0, 1, 0, 0);
                addMove(move);
            }
        }
    }

    public void generateBishopMoves() {
        int move;
        long bishops = isWhiteTurn ? bitboard.getPieces()[pieceMap.get('B')] : bitboard.getPieces()[pieceMap.get('b')];
        while (bishops != 0) {
            int fromSquare = BitUtils.getLs1bIndex(bishops);
            long toSquares = bitboardHelper.generateBishopAttacksWithMagics(fromSquare, bitboard.getOccupancies()[BOTH])
                    & (isWhiteTurn ? ~bitboard.getOccupancies()[WHITE] : ~bitboard.getOccupancies()[BLACK]);
            while (toSquares != 0) {
                int toSquare = BitUtils.getLs1bIndex(toSquares);
                boolean isCapture = isWhiteTurn ?
                        (bitboard.getOccupancies()[BLACK] & (1L << toSquare)) != 0 : (bitboard.getOccupancies()[WHITE] & (1L << toSquare)) != 0;

                move = MoveEncoder.encodeMove(fromSquare, toSquare, isWhiteTurn ? WhiteBishop : BlackBishop,
                        0, 0, 0, 0, isCapture ? 1 : 0);
                addMove(move);

                toSquares = BitUtils.popBit(toSquares, toSquare);
            }
            bishops = BitUtils.popBit(bishops, fromSquare);
        }
    }

    public void generateRookMoves() {
        int move;
        long rooks = isWhiteTurn ? bitboard.getPieces()[pieceMap.get('R')] : bitboard.getPieces()[pieceMap.get('r')];
        while (rooks != 0) {
            int fromSquare = BitUtils.getLs1bIndex(rooks);
            long toSquares = bitboardHelper.generateRookAttacksWithMagics(fromSquare, bitboard.getOccupancies()[BOTH])
                    & (isWhiteTurn ? ~bitboard.getOccupancies()[WHITE] : ~bitboard.getOccupancies()[BLACK]);

            while (toSquares != 0) {
                int toSquare = BitUtils.getLs1bIndex(toSquares);
                boolean isCapture = isWhiteTurn ?
                        (bitboard.getOccupancies()[BLACK] & (1L << toSquare)) != 0 : (bitboard.getOccupancies()[WHITE] & (1L << toSquare)) != 0;

                move = MoveEncoder.encodeMove(fromSquare, toSquare, isWhiteTurn ? WhiteRook : BlackRook,
                        0, 0, 0, 0, isCapture ? 1 : 0);
                addMove(move);

                toSquares = BitUtils.popBit(toSquares, toSquare);
            }
            rooks = BitUtils.popBit(rooks, fromSquare);
        }
    }

    public void generateQueenMoves() {
        int move;
        long queens = isWhiteTurn ? bitboard.getPieces()[pieceMap.get('Q')] : bitboard.getPieces()[pieceMap.get('q')];
        while (queens != 0) {
            int fromSquare = BitUtils.getLs1bIndex(queens);
            long toSquares = bitboardHelper.generateQueenAttacksWithMagics(fromSquare, bitboard.getOccupancies()[BOTH])
                    & (isWhiteTurn ? ~bitboard.getOccupancies()[WHITE] : ~bitboard.getOccupancies()[BLACK]);

            while (toSquares != 0) {
                int toSquare = BitUtils.getLs1bIndex(toSquares);
                boolean isCapture = isWhiteTurn ?
                        (bitboard.getOccupancies()[BLACK] & (1L << toSquare)) != 0 : (bitboard.getOccupancies()[WHITE] & (1L << toSquare)) != 0;

                move = MoveEncoder.encodeMove(fromSquare, toSquare, isWhiteTurn ? WhiteQueen : BlackQueen,
                        0, 0, 0, 0, isCapture ? 1 : 0);
                addMove(move);

                toSquares = BitUtils.popBit(toSquares, toSquare);
            }
            queens = BitUtils.popBit(queens, fromSquare);
        }
    }

    private boolean isSquareAttacked(int square, boolean isWhiteTurn) {
        if (!isWhiteTurn) {
            if ((bitboardHelper.pawnAttacks[WHITE][square] & bitboard.getPieces()[pieceMap.get('p')]) != 0) {
                return true;
            }
            if ((bitboardHelper.knightAttacks[square] & bitboard.getPieces()[pieceMap.get('n')]) != 0) {
                return true;
            }
            if ((bitboardHelper.kingAttacks[square] & bitboard.getPieces()[pieceMap.get('k')]) != 0) {
                return true;
            }
            if ((bitboardHelper.generateBishopAttacksWithMagics(square, bitboard.getOccupancies()[BOTH]) & bitboard.getPieces()[pieceMap.get('b')]) != 0) {
                return true;
            }
            if ((bitboardHelper.generateRookAttacksWithMagics(square, bitboard.getOccupancies()[BOTH]) & bitboard.getPieces()[pieceMap.get('r')]) != 0) {
                return true;
            }
            if ((bitboardHelper.generateQueenAttacksWithMagics(square, bitboard.getOccupancies()[BOTH]) & bitboard.getPieces()[pieceMap.get('q')]) != 0) {
                return true;
            }
        }
        else {
            if ((bitboardHelper.pawnAttacks[BLACK][square] & bitboard.getPieces()[pieceMap.get('P')]) != 0) {
                return true;
            }
            if ((bitboardHelper.knightAttacks[square] & bitboard.getPieces()[pieceMap.get('N')]) != 0) {
                return true;
            }
            if ((bitboardHelper.kingAttacks[square] & bitboard.getPieces()[pieceMap.get('K')]) != 0) {
                return true;
            }
            if ((bitboardHelper.generateBishopAttacksWithMagics(square, bitboard.getOccupancies()[BOTH]) & bitboard.getPieces()[pieceMap.get('B')]) != 0) {
                return true;
            }
            if ((bitboardHelper.generateRookAttacksWithMagics(square, bitboard.getOccupancies()[BOTH]) & bitboard.getPieces()[pieceMap.get('R')]) != 0) {
                return true;
            }
            if ((bitboardHelper.generateQueenAttacksWithMagics(square, bitboard.getOccupancies()[BOTH]) & bitboard.getPieces()[pieceMap.get('Q')]) != 0) {
                return true;
            }
        }
        return false;
    }

    private void addMove(int move) {
        move = tryMakeMove(move, isWhiteTurn);
        if (move != ILLEGAL) {
            moves[moveCounter++] = move;
        }
    }

    public int tryMakeMove(int move, boolean isWhiteTurn) {
        bitboard.backupState();
        bitboard.makeMove(move, isWhiteTurn, false);
        if (isWhiteTurn) {
            int whiteKingSquare = BitUtils.getLs1bIndex(bitboard.getPieces()[pieceMap.get('K')]);
            if (whiteKingSquare == -1) {
                bitboard.logBoardState();
            }
            if (isSquareAttacked(whiteKingSquare, !isWhiteTurn)) {
//                EngineLogger.debug("Encountered illegal move: ");
//                MoveEncoder.logMove(move);
                bitboard.restoreState();
                return ILLEGAL;
            }
        }
        else {
            int blackKingSquare = BitUtils.getLs1bIndex(bitboard.getPieces()[pieceMap.get('k')]);
            if (blackKingSquare == -1) {
                bitboard.logBoardState();
            }
            if (isSquareAttacked(blackKingSquare, !isWhiteTurn)) {
//                EngineLogger.debug("Encountered illegal move: ");
//                MoveEncoder.logMove(move);
                bitboard.restoreState();
                return ILLEGAL;
            }
        }
        bitboard.restoreState();
        return move;
    }

    private void logAttacks() {
        long bitboard = 0L;
        for (int square = 0; square < BOARD_SIZE; square++) {
            if (isSquareAttacked(square, isWhiteTurn)) bitboard |= (1L << square);
        }
        BitUtils.logBitboard(bitboard);
    }
}
