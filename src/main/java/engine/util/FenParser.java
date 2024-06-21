package engine.util;

import engine.core.Board;
import engine.core.Move;
import engine.core.Piece;
import engine.state.GameState;

import static engine.core.Piece.pieceMap;

// TODO better parsing
public class FenParser {
    public static GameState parseFEN(String fenString) {
        String[] parts = fenString.split(" ");

        // Piece placement is the first part
        String piecePlacement = parts[0];
        Board board = parsePieces(piecePlacement);

        // Active color (w or b)
        char activeColor = parts[1].charAt(0);
        boolean isWhiteTurn = true;
        if (activeColor == 'b') {
            isWhiteTurn = false;
        }

        // Castling availability
        String castlingAvailability = parts[2];

        // En passant target square
        String enPassantTargetSquare = parts[3];

        // Halfmove clock
        int halfmoveClock = Integer.parseInt(parts[4]);

        // Fullmove number
        int fullmoveNumber = Integer.parseInt(parts[5]);

        return new GameState(board, isWhiteTurn, castlingAvailability, enPassantTargetSquare, halfmoveClock, fullmoveNumber);
    }

    private static Board parsePieces(String piecePlacement) {
        byte[] state = new byte[120];
        for (int i = 0; i < 120; i++) {
            state[i] = Piece.BorderPiece;
        }

        String[] rows = piecePlacement.split("/");
        int rowIndex = 2;

        for (String row : rows) {
            int colIndex = 1;
            for (char c : row.toCharArray()) {
                if (Character.isDigit(c)) {
                    int emptySquares = c - '0';
                    for (int i = 0; i < emptySquares; i++) {
                        state[rowIndex * 10 + colIndex] = Piece.None;
                        colIndex++;
                    }
                }
                else {
                    state[rowIndex * 10 + colIndex] = pieceMap.get(c);
                    colIndex++;
                }
            }
            rowIndex++;
        }

        Board board = new Board(state);
        return board;
    }

    public static Move algebraicMoveToMove(String move) {
        int fromIndex = algebraicToIndex(move.substring(0, 2));
        int toIndex = algebraicToIndex(move.substring(2, 4));
        return new Move(fromIndex, toIndex);
    }

    public static String moveToAlgebraic(Move move) {
        return indexToAlgebraic(move.from) + indexToAlgebraic(move.to);
    }

    public static int algebraicToIndex(String position) {
        int file = position.charAt(0) - 'a' + 1;
        int rank = 10 - (position.charAt(1) - '0');
        return rank * 10 + file;
    }

    public static String indexToAlgebraic(int index) {
        int file = index % 10;
        int rank = 10 - (index / 10);
        char fileChar = (char) ('a' + file - 1);
        char rankChar = (char) ('0' + rank);
        return "" + fileChar + rankChar;
    }

}
