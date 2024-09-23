package engine.search;

import engine.core.entity.Piece;
import engine.core.state.Bitboard;
import engine.util.bits.BitUtils;

public class Evaluator {
    private static final int KNIGHT_PHASE = 1;
    private static final int BISHOP_PHASE = 1;
    private static final int ROOK_PHASE = 2;
    private static final int QUEEN_PHASE = 4;
    private static final int TOTAL_PHASE = KNIGHT_PHASE * 4 + BISHOP_PHASE * 4 + ROOK_PHASE * 4 + QUEEN_PHASE * 2;

    public int evaluatePieces(Bitboard bitboard) {
        int eval = 0;
        var pieces = bitboard.getPieces();
        for (int i = 0; i < Piece.PIECE_TYPES; i++) {
            eval += Piece.pieceToPieceEval[i] * Long.bitCount(pieces[i]);
        }
        return eval;
    }

    public int evaluateAdvanced(Bitboard bitboard) {
        int openingEval = 0, endgameEval = 0;
        var pieces = bitboard.getPieces();
        for (int i = 0; i < Piece.PIECE_TYPES; i++) {
            long pieceBitboard = pieces[i];
            while (pieceBitboard != 0) {
                int square = BitUtils.getLs1bIndex(pieceBitboard);

                openingEval += Piece.pieceToPieceEval[i]
                        + PieceSquareTables.evaluatePiece(i, square, false, i < Piece.WHITE_PIECE_TYPES);

                endgameEval += Piece.pieceToPieceEval[i]
                        + PieceSquareTables.evaluatePiece(i, square, true, i < Piece.WHITE_PIECE_TYPES);

                pieceBitboard = BitUtils.popBit(pieceBitboard, square);
            }
        }

        return getEval(pieces, openingEval, endgameEval);
    }

    private static int getEval(long[] pieces, int openingEval, int endgameEval) {
        int phase = TOTAL_PHASE;
        phase -= (Long.bitCount(pieces[Piece.WHITE_KNIGHT])
                + Long.bitCount(pieces[Piece.BLACK_KNIGHT])) * KNIGHT_PHASE;

        phase -= (Long.bitCount(pieces[Piece.WHITE_BISHOP])
                + Long.bitCount(pieces[Piece.BLACK_BISHOP])) * BISHOP_PHASE;

        phase -= (Long.bitCount(pieces[Piece.WHITE_ROOK])
                + Long.bitCount(pieces[Piece.BLACK_ROOK])) * ROOK_PHASE;

        phase -= (Long.bitCount(pieces[Piece.WHITE_QUEEN])
                + Long.bitCount(pieces[Piece.BLACK_QUEEN])) * QUEEN_PHASE;

        phase = (phase * 256 + (TOTAL_PHASE / 2)) / TOTAL_PHASE;
        return (openingEval * (256 - phase) + endgameEval * phase) / 256;
    }
}
