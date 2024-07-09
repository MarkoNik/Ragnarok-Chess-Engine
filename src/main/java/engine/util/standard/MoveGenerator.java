package engine.util.standard;

import app.EngineLogger;
import engine.core.entity.Board;
import engine.core.entity.Move;
import engine.core.entity.Piece;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {
    public static final int[] KNIGHT_MOVES = {21, 19, 12, 8, -8, -12, -19, -21};
    public static final int[] KING_QUEEN_MOVES = {10, -10, 1, -1, 11, 9, -9, -11};
    public static final int[] BISHOP_MOVES = {11, 9, -9, -11};
    public static final int[] ROOK_MOVES = {10, -10, 1, -1};

    private Board board;

    public List<Move> generateLegalMoves(boolean isWhiteTurn) {
        CheckHelper checkHelper = new CheckHelper();
        checkHelper.calculateAttackedSquares(!isWhiteTurn, board);
        return generateAllMoves(isWhiteTurn);
    }

    private List<Move> generateAllMoves(boolean isWhiteTurn) {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            byte piece = board.state[i];
            switch(board.state[i]) {
                case Piece.BorderPiece:
                case Piece.None:
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
        if (isWhiteTurn) {
            if (board.state[position - 10] == Piece.None) {
                moves.add(new Move(position, position - 10));
            }
            if (position > 80 && position < 90 && board.state[position - 10] == Piece.None && board.state[position - 20] == Piece.None) {
                moves.add(new Move(position, position - 20));
            }
            if (!Piece.isWhitePiece(board.state[position - 9]) && board.state[position - 9] != Piece.None && board.state[position - 9] != Piece.BorderPiece) {
                moves.add(new Move(position, position - 9));
            }
            if (!Piece.isWhitePiece(board.state[position - 11]) && board.state[position - 11] != Piece.None && board.state[position - 11] != Piece.BorderPiece) {
                moves.add(new Move(position, position - 11));
            }
            // TODO en passant
        }
        else {
            if (board.state[position + 10] == Piece.None) {
                moves.add(new Move(position, position + 10));
            }
            if (position > 30 && position < 40 && board.state[position + 10] == Piece.None && board.state[position + 20] == Piece.None) {
                moves.add(new Move(position, position + 20));
            }
            if (Piece.isWhitePiece(board.state[position + 9]) && board.state[position + 9] != Piece.None && board.state[position + 9] != Piece.BorderPiece) {
                moves.add(new Move(position, position + 9));
            }
            if (Piece.isWhitePiece(board.state[position + 11]) && board.state[position + 11] != Piece.None && board.state[position + 11] != Piece.BorderPiece) {
                moves.add(new Move(position, position + 11));
            }
            // TODO en passant
        }
        // TODO promotion
    }

    private void generateBishopMoves(int position, boolean isWhiteTurn, List<Move> moves) {
        // if the piece is not of the right color just return
        if ((Piece.isWhitePiece(board.state[position]) && !isWhiteTurn) ||
                (!Piece.isWhitePiece(board.state[position]) && isWhiteTurn)) {
            return;
        }
        if (isWhiteTurn) {
            int newPosition;
            for (int i = 0; i < 4; i++) {
                newPosition = position;
                while (true) {
                    newPosition += BISHOP_MOVES[i];
                    if (Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.BorderPiece) {
                        break;
                    }
                    moves.add(new Move(position, newPosition));
                    if (!Piece.isWhitePiece(board.state[newPosition])) {
                        break;
                    }
                }
            }
        }
        else {
            int newPosition;
            for (int i = 0; i < 4; i++) {
                newPosition = position;
                while (true) {
                    newPosition += BISHOP_MOVES[i];
                    if (!Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.BorderPiece) {
                        break;
                    }
                    moves.add(new Move(position, newPosition));
                    if (Piece.isWhitePiece(board.state[newPosition])) {
                        break;
                    }
                }
            }
        }
    }

    private void generateKnightMoves(int position, boolean isWhiteTurn, List<Move> moves) {
        // if the piece is not of the right color just return
        if ((Piece.isWhitePiece(board.state[position]) && !isWhiteTurn) ||
                (!Piece.isWhitePiece(board.state[position]) && isWhiteTurn)) {
            return;
        }
        if (isWhiteTurn) {
            int newPosition;
            for (int i = 0; i < 8; i++) {
                newPosition = position + KNIGHT_MOVES[i];
                if (Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.BorderPiece) {
                    continue;
                }
                moves.add(new Move(position, newPosition));
            }
        }
        else {
            int newPosition;
            for (int i = 0; i < 8; i++) {
                newPosition = position + KNIGHT_MOVES[i];
                if (!Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.BorderPiece) {
                    continue;
                }
                moves.add(new Move(position, newPosition));
            }
        }
    }

    private void generateRookMoves(int position, boolean isWhiteTurn, List<Move> moves) {
        // if the piece is not of the right color just return
        if ((Piece.isWhitePiece(board.state[position]) && !isWhiteTurn) ||
                (!Piece.isWhitePiece(board.state[position]) && isWhiteTurn)) {
            return;
        }
        if (isWhiteTurn) {
            int newPosition;
            for (int i = 0; i < 4; i++) {
                newPosition = position;
                while (true) {
                    newPosition += ROOK_MOVES[i];
                    if (Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.BorderPiece) {
                        break;
                    }
                    moves.add(new Move(position, newPosition));
                    if (!Piece.isWhitePiece(board.state[newPosition])) {
                        break;
                    }
                }
            }
        }
        else {
            int newPosition;
            for (int i = 0; i < 4; i++) {
                newPosition = position;
                while (true) {
                    newPosition += ROOK_MOVES[i];
                    if (!Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.BorderPiece) {
                        break;
                    }
                    moves.add(new Move(position, newPosition));
                    if (Piece.isWhitePiece(board.state[newPosition])) {
                        break;
                    }
                }
            }
        }
    }

    private void generateQueenMoves(int position, boolean isWhiteTurn, List<Move> moves) {
        // if the piece is not of the right color just return
        if ((Piece.isWhitePiece(board.state[position]) && !isWhiteTurn) ||
                (!Piece.isWhitePiece(board.state[position]) && isWhiteTurn)) {
            return;
        }
        if (isWhiteTurn) {
            int newPosition;
            for (int i = 0; i < 8; i++) {
                newPosition = position;
                while (true) {
                    newPosition += KING_QUEEN_MOVES[i];
                    if (Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.BorderPiece) {
                        break;
                    }
                    moves.add(new Move(position, newPosition));
                    if (!Piece.isWhitePiece(board.state[newPosition])) {
                        break;
                    }
                }
            }
        }
        else {
            int newPosition;
            for (int i = 0; i < 8; i++) {
                newPosition = position;
                while (true) {
                    newPosition += KING_QUEEN_MOVES[i];
                    if (!Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.BorderPiece) {
                        break;
                    }
                    moves.add(new Move(position, newPosition));
                    if (Piece.isWhitePiece(board.state[newPosition])) {
                        break;
                    }
                }
            }
        }
    }

    private void generateKingMoves(int position, boolean isWhiteTurn, List<Move> moves) {
        // if the piece is not of the right color just return
        if ((Piece.isWhitePiece(board.state[position]) && !isWhiteTurn) ||
                (!Piece.isWhitePiece(board.state[position]) && isWhiteTurn)) {
            return;
        }
        if (isWhiteTurn) {
            int newPosition;
            for (int i = 0; i < 8; i++) {
                newPosition = position + KING_QUEEN_MOVES[i];
                if (Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.BorderPiece) {
                    continue;
                }
                moves.add(new Move(position, newPosition));
            }
        }
        else {
            int newPosition;
            for (int i = 0; i < 8; i++) {
                newPosition = position + KING_QUEEN_MOVES[i];
                if (!Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.BorderPiece) {
                    continue;
                }
                moves.add(new Move(position, newPosition));
            }
        }

        // TODO castling
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
