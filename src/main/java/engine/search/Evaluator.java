package engine.search;

import engine.core.entity.Piece;
import engine.core.state.Bitboard;

public class Evaluator {
    public int evaluate(Bitboard bitboard) {
        int eval = 0;
        var pieces = bitboard.getPieces();
        for (int i = 0; i < Piece.MAX_PIECE; i++) {
            eval += Piece.pieceToPieceEval[i] * Long.bitCount(pieces[i]);
        }
        return eval;
    }
}
