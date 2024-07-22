package engine.search;

import engine.core.state.Bitboard;
import engine.core.state.TranspositionTable;

import java.util.ArrayList;
import java.util.List;

import static engine.core.entity.Piece.KING_VALUE;
import static engine.core.entity.Piece.QUEEN_VALUE;

public class Minimax {
    private Bitboard bitboard;
    private final MoveGenerator moveGenerator;
    private final Evaluator evaluator;
    private final TranspositionTable transpositionTable;
    private int ply;

    private final int CHECKMATE_VALUE = KING_VALUE + 10 * QUEEN_VALUE;
    public Minimax(MoveGenerator moveGenerator, Evaluator evaluator, TranspositionTable transpositionTable) {
        this.moveGenerator = moveGenerator;
        this.evaluator = evaluator;
        this.transpositionTable = transpositionTable;
    }

    public int search(int depth, int alpha, int beta, boolean isWhiteTurn) {
        TranspositionTable.TTEntry entry = transpositionTable.get(bitboard.getHash());
        if (entry != null && entry.key() == bitboard.getHash() && entry.depth() >= depth) {
            if (entry.flag() == transpositionTable.EXACT_FLAG) {
                return entry.score();
            }
            else if (entry.flag() == transpositionTable.ALPHA_FLAG) {
                alpha = Math.max(alpha, entry.score());
            }
            else if (entry.flag() == transpositionTable.BETA_FLAG) {
                beta = Math.min(beta, entry.score());
            }
            if (alpha >= beta) {
                return entry.score();
            }
        }

        if (depth == 0) {
            int eval = (isWhiteTurn ? 1 : -1) * evaluator.evaluate(bitboard);
            return eval;
        }

        int previousAlpha = alpha;
        int bestMove = -1;

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
                if (moves[i] == -1) System.out.println("No best move found beta?");
                transpositionTable.put(bitboard.getHash(), moves[i], beta, depth, transpositionTable.BETA_FLAG);
                return beta;
            }

            if (score > alpha) {
                alpha = score;
                bestMove = moves[i];
            }
        }

        boolean check = moveGenerator.isKingInCheck(isWhiteTurn);
        if (moveCounter == 0) {
            if (check) {
                return -CHECKMATE_VALUE + ply;
            } else {
                return 0;
            }
        }

        transpositionTable.put(bitboard.getHash(), bestMove, alpha, depth, transpositionTable.ALPHA_FLAG);
        return alpha;
    }

    public List<Integer> getPrincipalVariation(boolean isWhiteTurn) {
        List<Integer> pv = new ArrayList<>();
        while (true) {
            TranspositionTable.TTEntry entry = transpositionTable.get(bitboard.getHash());
            if (entry == null || entry.bestMove() == -1) {
                break;
            }
            pv.add(entry.bestMove());
            bitboard.backupState();
            bitboard.makeMove(entry.bestMove(), isWhiteTurn, false);
            isWhiteTurn = !isWhiteTurn;
        }
        for (int i = pv.size() - 1; i >= 0; i--) {
            bitboard.restoreState();
        }
        return pv;
    }

    public void setBitboard(Bitboard bitboard) {
        this.bitboard = bitboard;
        ply = 0;
    }
}
