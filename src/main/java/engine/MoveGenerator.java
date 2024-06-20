package engine;

import app.EngineLogger;
import engine.core.Board;
import engine.core.Move;
import engine.core.Piece;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {
    private static final int[] KNIGHT_MOVES = {21, 19, 12, 8, -8, -12, -19, -21};
    private static final int[] KING_QUEEN_MOVES = {10, -10, 1, -1, 11, 9, -9, -11};
    private static final int[] BISHOP_MOVES = {11, 9, -9, -11};
    private static final int[] ROOK_MOVES = {10, -10, 1, -1};

    private Board board;

    public MoveGenerator(Board board) {
        this.board = board;
    }

    public List<Move> generateAllMoves(boolean isWhiteTurn) {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            byte piece = board.state[i];
            switch(board.state[i]) {
                case Piece.BorderPiece:
                    break;
                case Piece.WhitePawn:
                case Piece.BlackPawn:
                    generatePawnMoves(i, isWhiteTurn, moves);
                    break;
                case Piece.WhiteBishop:
                case Piece.BlackBishop:
                    generateBishopMoves(i, isWhiteTurn, moves);
                    break;
                case Piece.WhiteKnight:
                case Piece.BlackKnight:
                    generateKnightMoves(i, isWhiteTurn, moves);
                    break;
                case Piece.WhiteRook:
                case Piece.BlackRook:
                    generateRookMoves(i, isWhiteTurn, moves);
                    break;
                case Piece.WhiteQueen:
                case Piece.BlackQueen:
                    generateQueenMoves(i, isWhiteTurn, moves);
                    break;
                case Piece.WhiteKing:
                case Piece.BlackKing:
                    generateKingMoves(i, isWhiteTurn, moves);
                    break;
                default:
                    EngineLogger.error("Invalid piece found on board: " + piece);
            }
        }
        return moves;
    }

    private void generatePawnMoves(int position, boolean isWhiteTurn, List<Move> moves) {
        // if the piece is not of the right color just return
        if ((Piece.isWhitePiece(board.state[position]) && !isWhiteTurn) ||
                (!Piece.isWhitePiece(board.state[position]) && isWhiteTurn)) {
            return;
        }

    }

    private void generateBishopMoves(int position, boolean isWhiteTurn, List<Move> moves) {
        // if the piece is not of the right color just return
        if ((Piece.isWhitePiece(board.state[position]) && !isWhiteTurn) ||
                (!Piece.isWhitePiece(board.state[position]) && isWhiteTurn)) {
            return;
        }

    }

    private void generateKnightMoves(int position, boolean isWhiteTurn, List<Move> moves) {
        // if the piece is not of the right color just return
        if ((Piece.isWhitePiece(board.state[position]) && !isWhiteTurn) ||
                (!Piece.isWhitePiece(board.state[position]) && isWhiteTurn)) {
            return;
        }

    }

    private void generateRookMoves(int position, boolean isWhiteTurn, List<Move> moves) {
        // if the piece is not of the right color just return
        if ((Piece.isWhitePiece(board.state[position]) && !isWhiteTurn) ||
                (!Piece.isWhitePiece(board.state[position]) && isWhiteTurn)) {
            return;
        }

    }

    private void generateQueenMoves(int position, boolean isWhiteTurn, List<Move> moves) {
        // if the piece is not of the right color just return
        if ((Piece.isWhitePiece(board.state[position]) && !isWhiteTurn) ||
                (!Piece.isWhitePiece(board.state[position]) && isWhiteTurn)) {
            return;
        }

    }

    private void generateKingMoves(int position, boolean isWhiteTurn, List<Move> moves) {
        // if the piece is not of the right color just return
        if ((Piece.isWhitePiece(board.state[position]) && !isWhiteTurn) ||
                (!Piece.isWhitePiece(board.state[position]) && isWhiteTurn)) {
            return;
        }

    }


}
