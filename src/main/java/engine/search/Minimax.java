package engine.search;

import engine.core.state.Bitboard;

import static engine.core.entity.Piece.KING_VALUE;
import static engine.core.entity.Piece.QUEEN_VALUE;

public class Minimax {
    private Bitboard bitboard;
    private final MoveGenerator moveGenerator;
    private final Evaluator evaluator;
    private int bestMove = -1;
    private int ply;

    private final int CHECKMATE_VALUE = KING_VALUE + 10 * QUEEN_VALUE;
    public Minimax(MoveGenerator moveGenerator, Evaluator evaluator) {
        this.moveGenerator = moveGenerator;
        this.evaluator = evaluator;
    }

    public int search(int depth, int alpha, int beta, boolean isWhiteTurn) {
        if (depth == 0) {
            return (isWhiteTurn ? 1 : -1) * evaluator.evaluate(bitboard);
        }

        int previousAlpha = alpha;
        int currentBestMove = -1;

        int[] moves = moveGenerator.generateLegalMoves(isWhiteTurn).clone();
        int moveCounter = moveGenerator.getMoveCounter();
        moveGenerator.clearMoves();
        for (int i = 0; i < moveCounter; i++) {
            bitboard.backupState();
            bitboard.makeMove(moves[i], isWhiteTurn, false);
            ply++;

            int score = -search(depth - 1, -beta, -alpha, !isWhiteTurn);

            ply--;
            bitboard.restoreState();

            if (score >= beta) {
                return beta;
            }

            if (score > alpha) {
                alpha = score;
                if (ply == 0) {
                    currentBestMove = moves[i];
                }
            }
        }

        // checkmate and stalemate handling
        boolean check = moveGenerator.isKingInCheck(isWhiteTurn);
        if (moveCounter == 0) {
            if (check) {
                return -CHECKMATE_VALUE + ply;
            }
            else return 0;
        }

        if (previousAlpha != alpha) {
            bestMove = currentBestMove;
        }

        return alpha;
    }

    public void setBitboard(Bitboard bitboard) {
        this.bitboard = bitboard;
        ply = 0;
    }

    public int getBestMove() {
        return bestMove;
    }
}
