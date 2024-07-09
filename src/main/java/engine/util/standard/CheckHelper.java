//package engine.util.standard;
//
//import app.EngineLogger;
//import engine.core.entity.Board;
//import engine.core.entity.Piece;
//
//import static engine.core.entity.Board.*;
//import static engine.util.standard.MoveGenerator.*;
//
//public class CheckHelper {
//    private boolean[] attacked = new boolean[BOARD_SIZE];
//    private Board board;
//    public void calculateAttackedSquares(boolean whiteAttacks, Board board) {
//        this.board = board;
//        for (int i = 0; i < BOARD_SIZE; i++) {
//            byte piece = board.state[i];
//            switch(board.state[i]) {
//                case Piece.MaxPiece:
//                case Piece.None:
//                    break;
//                case Piece.WhitePawn:
//                case Piece.BlackPawn:
//                    calculatePawnAttacks(i, whiteAttacks);
//                    break;
//                case Piece.WhiteBishop:
//                case Piece.BlackBishop:
//                    calculateBishopAttacks(i, whiteAttacks);
//                    break;
//                case Piece.WhiteKnight:
//                case Piece.BlackKnight:
//                    calculateKnightAttacks(i, whiteAttacks);
//                    break;
//                case Piece.WhiteRook:
//                case Piece.BlackRook:
//                    calculateRookAttacks(i, whiteAttacks);
//                    break;
//                case Piece.WhiteQueen:
//                case Piece.BlackQueen:
//                    calculateQueenAttacks(i, whiteAttacks);
//                    break;
//                case Piece.WhiteKing:
//                case Piece.BlackKing:
//                    calculateKingAttacks(i, whiteAttacks);
//                    break;
//                default:
//                    EngineLogger.error("Invalid piece found on board: " + piece);
//            }
//        }
//        logState();
//    }
//
//    private void calculatePawnAttacks(int position, boolean whiteAttacks) {
//        // if the piece is not of the right color just return
//        if ((Piece.isWhitePiece(board.state[position]) && !whiteAttacks) ||
//                (Piece.isBlackPiece(board.state[position]) && whiteAttacks)) {
//            return;
//        }
//        if (whiteAttacks) {
//            attacked[position - 9] = true;
//            attacked[position - 11] = true;
//        }
//        else {
//            attacked[position + 9] = true;
//            attacked[position + 11] = true;
//        }
//    }
//
//    private void calculateBishopAttacks(int position, boolean whiteAttacks) {
//        // if the piece is not of the right color just return
//        if ((Piece.isWhitePiece(board.state[position]) && !whiteAttacks) ||
//                (Piece.isBlackPiece(board.state[position]) && whiteAttacks)) {
//            return;
//        }
//        if (whiteAttacks) {
//            int newPosition;
//            for (int i = 0; i < 4; i++) {
//                newPosition = position;
//                while (true) {
//                    newPosition += BISHOP_MOVES[i];
//                    if (Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.MaxPiece) {
//                        break;
//                    }
//                    attacked[newPosition] = true;
//                    if (Piece.isBlackPiece(board.state[newPosition])) {
//                        break;
//                    }
//                }
//            }
//        }
//        else {
//            int newPosition;
//            for (int i = 0; i < 4; i++) {
//                newPosition = position;
//                while (true) {
//                    newPosition += BISHOP_MOVES[i];
//                    if (Piece.isBlackPiece(board.state[newPosition]) || board.state[newPosition] == Piece.MaxPiece) {
//                        break;
//                    }
//                    attacked[newPosition] = true;
//                    if (Piece.isWhitePiece(board.state[newPosition])) {
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//    private void calculateKnightAttacks(int position, boolean whiteAttacks) {
//        // if the piece is not of the right color just return
//        if ((Piece.isWhitePiece(board.state[position]) && !whiteAttacks) ||
//                (Piece.isBlackPiece(board.state[position]) && whiteAttacks)) {
//            return;
//        }
//        if (whiteAttacks) {
//            int newPosition;
//            for (int i = 0; i < 8; i++) {
//                newPosition = position + KNIGHT_MOVES[i];
//                attacked[newPosition] = true;
//            }
//        }
//        else {
//            int newPosition;
//            for (int i = 0; i < 8; i++) {
//                newPosition = position + KNIGHT_MOVES[i];
//                attacked[newPosition] = true;
//            }
//        }
//    }
//
//    private void calculateRookAttacks(int position, boolean whiteAttacks) {
//        // if the piece is not of the right color just return
//        if ((Piece.isWhitePiece(board.state[position]) && !whiteAttacks) ||
//                (Piece.isBlackPiece(board.state[position]) && whiteAttacks)) {
//            return;
//        }
//        if (whiteAttacks) {
//            int newPosition;
//            for (int i = 0; i < 4; i++) {
//                newPosition = position;
//                while (true) {
//                    newPosition += ROOK_MOVES[i];
//                    if (Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.MaxPiece) {
//                        break;
//                    }
//                    attacked[newPosition] = true;
//                    if (Piece.isBlackPiece(board.state[newPosition])) {
//                        break;
//                    }
//                }
//            }
//        }
//        else {
//            int newPosition;
//            for (int i = 0; i < 4; i++) {
//                newPosition = position;
//                while (true) {
//                    newPosition += ROOK_MOVES[i];
//                    if (Piece.isBlackPiece(board.state[newPosition]) || board.state[newPosition] == Piece.MaxPiece) {
//                        break;
//                    }
//                    attacked[newPosition] = true;
//                    if (Piece.isWhitePiece(board.state[newPosition])) {
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//    private void calculateQueenAttacks(int position, boolean whiteAttacks) {
//        // if the piece is not of the right color just return
//        if ((Piece.isWhitePiece(board.state[position]) && !whiteAttacks) ||
//                (Piece.isBlackPiece(board.state[position]) && whiteAttacks)) {
//            return;
//        }
//        if (whiteAttacks) {
//            int newPosition;
//            for (int i = 0; i < 8; i++) {
//                newPosition = position;
//                while (true) {
//                    newPosition += KING_QUEEN_MOVES[i];
//                    if (Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.MaxPiece) {
//                        break;
//                    }
//                    attacked[newPosition] = true;
//                    if (Piece.isBlackPiece(board.state[newPosition])) {
//                        break;
//                    }
//                }
//            }
//        }
//        else {
//            int newPosition;
//            for (int i = 0; i < 8; i++) {
//                newPosition = position;
//                while (true) {
//                    newPosition += KING_QUEEN_MOVES[i];
//                    if (Piece.isBlackPiece(board.state[newPosition]) || board.state[newPosition] == Piece.MaxPiece) {
//                        break;
//                    }
//                    attacked[newPosition] = true;
//                    if (Piece.isWhitePiece(board.state[newPosition])) {
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//    private void calculateKingAttacks(int position, boolean whiteAttacks) {
//        // if the piece is not of the right color just return
//        if ((Piece.isWhitePiece(board.state[position]) && !whiteAttacks) ||
//                (Piece.isBlackPiece(board.state[position]) && whiteAttacks)) {
//            return;
//        }
//        if (whiteAttacks) {
//            int newPosition;
//            for (int i = 0; i < 8; i++) {
//                newPosition = position + KING_QUEEN_MOVES[i];
//                if (Piece.isWhitePiece(board.state[newPosition]) || board.state[newPosition] == Piece.MaxPiece) {
//                    continue;
//                }
//                attacked[newPosition] = true;
//            }
//        }
//        else {
//            int newPosition;
//            for (int i = 0; i < 8; i++) {
//                newPosition = position + KING_QUEEN_MOVES[i];
//                if (Piece.isBlackPiece(board.state[newPosition]) || board.state[newPosition] == Piece.MaxPiece) {
//                    continue;
//                }
//                attacked[newPosition] = true;
//            }
//        }
//    }
//
//    public void logState() {
//        StringBuilder sb = new StringBuilder();
//        for (int i = BOARD_START; i < BOARD_END; i++) {
//            if (board.state[i] == Piece.MaxPiece) {
//                if (i % COLS == 0) {
//                    sb.append("\n");
//                }
//                continue;
//            }
//            sb.append(attacked[i]?'1':'0').append(" ");
//        }
//        EngineLogger.debug(sb.toString());
//    }
//}
