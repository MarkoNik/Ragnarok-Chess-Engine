package engine.util;

import app.Constants;
import engine.core.entity.Piece;
import engine.core.bitboard.BitboardHelper;
import engine.core.state.Bitboard;

import static app.Constants.*;

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
        logAttacks();
    }

    public int getMoveCounter() {
        return moveCounter;
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

                move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.WhitePawn,
                        0, 0, 0, 0, 0);
                if (move != ILLEGAL) {
                    moves[moveCounter++] = move;
                }

                if (toSquare <= RANK_8_END_SQUARE) {
                    move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.WhitePawn,
                            Piece.WhiteQueen, 0, 0, 0, 0);
                    if (move != ILLEGAL) {
                        moves[moveCounter++] = move;
                    }

                    move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.WhitePawn,
                            Piece.WhiteRook, 0,0, 0, 0);
                    if (move != ILLEGAL) {
                        moves[moveCounter++] = move;
                    }

                    move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.WhitePawn,
                            Piece.WhiteBishop, 0, 0, 0, 0);
                    if (move != ILLEGAL) {
                        moves[moveCounter++] = move;
                    }

                    move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.WhitePawn,
                            Piece.WhiteKnight, 0, 0, 0, 0);
                    if (move != ILLEGAL) {
                        moves[moveCounter++] = move;
                    }
                }
                whitePawnTargets = BitUtils.popBit(whitePawnTargets, toSquare);
            }
        }
        else {
            long blackPawnTargets = (bitboard.getOccupancies()[pieceMap.get('p')] << 8) & ~bitboard.getOccupancies()[BOTH];
            while (blackPawnTargets != 0) {
                int toSquare = BitUtils.getLs1bIndex(blackPawnTargets);
                int fromSquare = toSquare - 8;
                move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.BlackPawn,
                        0, 0, 0, 0, 0);
                if (move != ILLEGAL) {
                    moves[moveCounter++] = move;
                }

                if (toSquare >= RANK_1_START_SQUARE) {
                    move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.BlackPawn,
                            Piece.BlackQueen, 0, 0, 0, 0);
                    if (move != ILLEGAL) {
                        moves[moveCounter++] = move;
                    }

                    move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.BlackPawn,
                            Piece.BlackRook, 0,0, 0, 0);
                    if (move != ILLEGAL) {
                        moves[moveCounter++] = move;
                    }

                    move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.BlackPawn,
                            Piece.BlackBishop, 0, 0, 0, 0);
                    if (move != ILLEGAL) {
                        moves[moveCounter++] = move;
                    }

                    move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.BlackPawn,
                            Piece.BlackKnight, 0, 0, 0, 0);
                    if (move != ILLEGAL) {
                        moves[moveCounter++] = move;
                    }
                }
                blackPawnTargets = BitUtils.popBit(blackPawnTargets, toSquare);
            }
        }
    }

    public void generateDoublePawnPushMoves() {
        int move;
        if (isWhiteTurn) {
            long whiteSinglePushTargets = (bitboard.getPieces()[pieceMap.get('P')] << 8) & ~bitboard.getOccupancies()[BOTH];
            long whiteDoublePushTargets = (whiteSinglePushTargets << 8) & ~bitboard.getOccupancies()[BOTH] & RANK_4;
            while (whiteDoublePushTargets != 0) {
                int toSquare = BitUtils.getLs1bIndex(whiteDoublePushTargets);
                int fromSquare = toSquare + 16;

                move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.WhitePawn,
                        0, 1, 0, 0, 0);
                if (move != ILLEGAL) {
                    moves[moveCounter++] = move;
                }

                whiteDoublePushTargets = BitUtils.popBit(whiteDoublePushTargets, toSquare);
            }
        }
        else {
            long blackSinglePushTargets = (bitboard.getOccupancies()[pieceMap.get('p')] >>> 8) & ~bitboard.getOccupancies()[BOTH];
            long blackDoublePushTargets = (blackSinglePushTargets >>> 8) & ~bitboard.getOccupancies()[BOTH] & RANK_5;
            while (blackDoublePushTargets != 0) {
                int toSquare = BitUtils.getLs1bIndex(blackDoublePushTargets);
                int fromSquare = toSquare - 16;

                move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.BlackPawn,
                        0, 1, 0, 0, 0);
                if (move != ILLEGAL) {
                    moves[moveCounter++] = move;
                }

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
                        && (bitboardHelper.pawnAttacks[WHITE][fromSquare] & (1L << bitboard.getEnPassantSquare())) != 0) {
                    move = MoveEncoder.encodeMove(fromSquare, bitboard.getEnPassantSquare() - 8, Piece.WhitePawn,
                            0, 0, 0, 1, 1);
                    if (move != ILLEGAL) {
                        moves[moveCounter++] = move;
                    }
                }

                else if (fromSquare >= RANK_7_START_SQUARE && fromSquare <= RANK_7_END_SQUARE) {
                    while (toSquares != 0) {
                        int toSquare = BitUtils.getLs1bIndex(toSquares);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.WhitePawn,
                                Piece.WhiteQueen, 0, 0, 0, 1);
                        if (move != ILLEGAL) {
                            moves[moveCounter++] = move;
                        }

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.WhitePawn,
                                Piece.WhiteRook, 0, 0, 0, 1);
                        if (move != ILLEGAL) {
                            moves[moveCounter++] = move;
                        }

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.WhitePawn,
                                Piece.WhiteBishop, 0, 0, 0, 1);
                        if (move != ILLEGAL) {
                            moves[moveCounter++] = move;
                        }

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.WhitePawn,
                                Piece.WhiteKnight, 0, 0, 0, 1);
                        if (move != ILLEGAL) {
                            moves[moveCounter++] = move;
                        }

                        toSquares = BitUtils.popBit(toSquares, toSquare);
                    }
                }

                else {
                    while (toSquares != 0) {
                        int toSquare = BitUtils.getLs1bIndex(toSquares);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.WhitePawn,
                                0, 0, 0, 0, 1);
                        if (move != ILLEGAL) {
                            moves[moveCounter++] = move;
                        }

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
                        && (bitboardHelper.pawnAttacks[BLACK][fromSquare] & (1L << bitboard.getEnPassantSquare())) != 0) {
                    move = MoveEncoder.encodeMove(fromSquare, bitboard.getEnPassantSquare() + 8, Piece.BlackPawn,
                            0, 0, 0, 1, 1);
                    if (move != ILLEGAL) {
                        moves[moveCounter++] = move;
                    }
                }

                else if (fromSquare >= RANK_2_START_SQUARE && fromSquare <= RANK_2_END_SQUARE) {
                    while (toSquares != 0) {
                        int toSquare = BitUtils.getLs1bIndex(toSquares);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.BlackPawn,
                                Piece.BlackQueen, 0, 0, 0, 1);
                        if (move != ILLEGAL) {
                            moves[moveCounter++] = move;
                        }

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.BlackPawn,
                                Piece.BlackRook, 0, 0, 0, 1);
                        if (move != ILLEGAL) {
                            moves[moveCounter++] = move;
                        }

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.BlackPawn,
                                Piece.BlackBishop, 0, 0, 0, 1);
                        if (move != ILLEGAL) {
                            moves[moveCounter++] = move;
                        }

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.BlackPawn,
                                Piece.BlackKnight, 0, 0, 0, 1);
                        if (move != ILLEGAL) {
                            moves[moveCounter++] = move;
                        }

                        toSquares = BitUtils.popBit(toSquares, toSquare);
                    }
                }

                else {
                    while (toSquares != 0) {
                        int toSquare = BitUtils.getLs1bIndex(toSquares);

                        move = MoveEncoder.encodeMove(fromSquare, toSquare, Piece.BlackPawn,
                                0, 0, 0, 0, 1);
                        if (move != ILLEGAL) {
                            moves[moveCounter++] = move;
                        }

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

                move = MoveEncoder.encodeMove(fromSquare, toSquare, isWhiteTurn ? Piece.WhiteKnight : Piece.BlackKnight,
                        0, 0, 0, 0, isCapture ? 1 : 0);
                if (move != ILLEGAL) {
                    moves[moveCounter++] = move;
                }

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

            move = MoveEncoder.encodeMove(fromSquare, toSquare, isWhiteTurn ? Piece.WhiteKing : Piece.BlackKing,
                    0, 0, 0, 0, isCapture ? 1 : 0);
            if (move != ILLEGAL) {
                moves[moveCounter++] = move;
            }

            toSquares = BitUtils.popBit(toSquares, toSquare);
        }

        if (isWhiteTurn) {
            if ((bitboard.getCastlesFlags() & WHITE_KINGSIDE_CASTLES_MASK) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_KINGSIDE_ROOK - 1))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_KINGSIDE_ROOK - 2))) != 0
                    && !isSquareAttacked(WHITE_KINGSIDE_ROOK - 1, false)
                    && !isSquareAttacked(WHITE_KINGSIDE_ROOK - 2, false)
                    && !isSquareAttacked(WHITE_KINGSIDE_ROOK - 3, false)) {

                move = MoveEncoder.encodeMove(fromSquare, WHITE_KINGSIDE_CASTLES_SQUARE, Piece.WhiteKing,
                        0, 0, 1, 0, 0);
                if (move != ILLEGAL) {
                    moves[moveCounter++] = move;
                }
            }

            if ((bitboard.getCastlesFlags() & WHITE_QUEENSIDE_CASTLES_MASK) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_QUEENSIDE_ROOK + 1))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_QUEENSIDE_ROOK + 2))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (WHITE_QUEENSIDE_ROOK + 3))) != 0
                    && !isSquareAttacked(WHITE_QUEENSIDE_ROOK + 2, false)
                    && !isSquareAttacked(WHITE_QUEENSIDE_ROOK + 3, false)
                    && !isSquareAttacked(WHITE_QUEENSIDE_ROOK + 4, false)) {

                move = MoveEncoder.encodeMove(fromSquare, WHITE_QUEENSIDE_CASTLES_SQUARE, Piece.WhiteKing,
                        0, 0, 1, 0, 0);
                if (move != ILLEGAL) {
                    moves[moveCounter++] = move;
                }
            }
        }
        else {
            if ((bitboard.getCastlesFlags() & BLACK_KINGSIDE_CASTLES_MASK) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_KINGSIDE_ROOK - 1))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_KINGSIDE_ROOK - 2))) != 0
                    && !isSquareAttacked(BLACK_KINGSIDE_ROOK - 1, true)
                    && !isSquareAttacked(BLACK_KINGSIDE_ROOK - 2, true)
                    && !isSquareAttacked(BLACK_KINGSIDE_ROOK - 3, true)) {

                move = MoveEncoder.encodeMove(fromSquare, BLACK_KINGSIDE_CASTLES_SQUARE, Piece.BlackKing,
                        0, 0, 1, 0, 0);
                if (move != ILLEGAL) {
                    moves[moveCounter++] = move;
                }
            }
            if ((bitboard.getCastlesFlags() & BLACK_QUEENSIDE_CASTLES_MASK) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_QUEENSIDE_ROOK + 1))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_QUEENSIDE_ROOK + 2))) != 0
                    && (bitboard.getOccupancies()[BOTH] & (1L << (BLACK_QUEENSIDE_ROOK + 3))) != 0
                    && !isSquareAttacked(BLACK_QUEENSIDE_ROOK + 2, false)
                    && !isSquareAttacked(BLACK_QUEENSIDE_ROOK + 3, false)
                    && !isSquareAttacked(BLACK_QUEENSIDE_ROOK + 4, false)) {

                move = MoveEncoder.encodeMove(fromSquare, BLACK_QUEENSIDE_CASTLES_SQUARE, Piece.BlackKing,
                        0, 0, 1, 0, 0);
                if (move != ILLEGAL) {
                    moves[moveCounter++] = move;
                }
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

                move = MoveEncoder.encodeMove(fromSquare, toSquare, isWhiteTurn ? Piece.WhiteBishop : Piece.BlackBishop,
                        0, 0, 0, 0, isCapture ? 1 : 0);
                if (move != ILLEGAL) {
                    moves[moveCounter++] = move;
                }

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

                move = MoveEncoder.encodeMove(fromSquare, toSquare, isWhiteTurn ? Piece.WhiteRook : Piece.BlackRook,
                        0, 0, 0, 0, isCapture ? 1 : 0);
                if (move != ILLEGAL) {
                    moves[moveCounter++] = move;
                }

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

                move = MoveEncoder.encodeMove(fromSquare, toSquare, isWhiteTurn ? Piece.WhiteQueen : Piece.BlackQueen,
                        0, 0, 0, 0, isCapture ? 1 : 0);
                if (move != ILLEGAL) {
                    moves[moveCounter++] = move;
                }

                toSquares = BitUtils.popBit(toSquares, toSquare);
            }
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
