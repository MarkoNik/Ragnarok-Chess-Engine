package engine.search;

import engine.core.state.Bitboard;

import static app.Constants.INF;

public class Minimax {
    private Bitboard bitboard;
    private MoveGenerator moveGenerator;
    private Evaluator evaluator;
    private int bestMove = -1;
    private int ply;

    public Minimax(MoveGenerator moveGenerator, Evaluator evaluator) {
        this.moveGenerator = moveGenerator;
        this.evaluator = evaluator;
    }

    public int search(int depth, boolean isWhiteTurn) {
        if (depth == 0) {
            return (isWhiteTurn ? 1 : -1) * evaluator.evaluate(bitboard);
        }

        int best = -INF;
        int[] moves = moveGenerator.generateLegalMoves(isWhiteTurn).clone();
        int moveCounter = moveGenerator.getMoveCounter();
        moveGenerator.clearMoves();
        for (int i = 0; i < moveCounter; i++) {
            bitboard.backupState();
            bitboard.makeMove(moves[i], isWhiteTurn, false);
            ply++;
            int score = -search(depth - 1, !isWhiteTurn);
            ply--;
            bitboard.restoreState();
            if (best < score) {
                best = score;
                if (ply == 0) {
                    bestMove = moves[i];
                }
            }
        }
        boolean check = moveGenerator.isKingInCheck(isWhiteTurn);
        if (moveCounter == 0) {
            if (check) {
                return -49000 + ply;
            }
            else return 0;
        }
        return best;
    }

    public void setBitboard(Bitboard bitboard) {
        this.bitboard = bitboard;
        ply = 0;
    }

    public int getBestMove() {
        return bestMove;
    }
}
