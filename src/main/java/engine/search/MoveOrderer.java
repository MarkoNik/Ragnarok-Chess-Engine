package engine.search;

import engine.core.entity.Piece;
import engine.core.state.Bitboard;
import engine.util.bits.BitUtils;
import engine.util.bits.MoveEncoder;

import java.util.List;

import static engine.core.entity.Piece.PIECE_TYPES;

public class MoveOrderer {
    public record CaptureMoveWrapper(int move, int victimValue, int attackerValue) {}
    private Bitboard bitboard;

    // In place sorting
    public void sortCaptures(List<Integer> moves, Bitboard bitboard) {
        this.bitboard = bitboard;
        List<CaptureMoveWrapper> captures = moves.stream()
                .map(this::wrapCapture)
                .sorted((capture1, capture2) -> {
                    int victimComparison = Integer.compare(capture2.victimValue(), capture1.victimValue());
                    if (victimComparison != 0) {
                        return victimComparison;
                    }

                    // If the victims are the same, compare the value of the attackers
                    return Integer.compare(capture1.attackerValue(), capture2.attackerValue());
                })
                .toList();

        moves.clear();
        for (var capture : captures) {
            moves.add(capture.move());
        }
    }

    private CaptureMoveWrapper wrapCapture(int move) {
        int attacker = MoveEncoder.extractPiece(move);
        int victim = -1;
        int to = MoveEncoder.extractTo(move);

        int enPassant = MoveEncoder.extractEnPassantFlag(move);
        if (enPassant == 1) {
            return new CaptureMoveWrapper(move, Piece.PAWN_VALUE, Piece.PAWN_VALUE);
        }

        var pieces = bitboard.getPieces();
        for (int i = 0; i < PIECE_TYPES; i++) {
            if (BitUtils.getBit(pieces[i], to)) {
                victim = i;
                break;
            }
        }
        return new CaptureMoveWrapper(move, Math.abs(Piece.pieceToPieceEval[victim]), Math.abs(Piece.pieceToPieceEval[attacker]));
    }

    public void sortPVFirst(int[] moves, int moveCounter, int bestMove) {
        for (int i = 0; i < moveCounter; i++) {
            if (moves[i] == bestMove) {
                int tempMove = moves[0];
                moves[0] = moves[i];
                moves[i] = tempMove;
                return;
            }
        }
    }
}
