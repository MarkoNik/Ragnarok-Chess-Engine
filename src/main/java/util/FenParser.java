package util;

import engine.Board;
import engine.GameState;
import engine.Piece;

import static engine.Piece.pieceMap;

// TODO better parsing
public class FenParser {
    public static GameState parseFEN(String fenString) {
        String[] parts = fenString.split(" ");

        // Piece placement is the first part
        String piecePlacement = parts[0];
        Board board = parsePieces(piecePlacement);

        // Active color (w or b)
        char activeColor = parts[1].charAt(0);

        // Castling availability
        String castlingAvailability = parts[2];

        // En passant target square
        String enPassantTargetSquare = parts[3];

        // Halfmove clock
        int halfmoveClock = Integer.parseInt(parts[4]);

        // Fullmove number
        int fullmoveNumber = Integer.parseInt(parts[5]);

        return new GameState(board, activeColor, castlingAvailability, enPassantTargetSquare, halfmoveClock, fullmoveNumber);
    }

    private static Board parsePieces(String piecePlacement) {
        byte[] state = new byte[120];
        // Fill the board with border piece to denote the 10x12 structure
        for (int i = 0; i < 120; i++) {
            state[i] = Piece.BorderPiece;
        }

        String[] rows = piecePlacement.split("/");
        int rowIndex = 2; // Start from the third row to handle 10x12 structure

        for (String row : rows) {
            int colIndex = 1; // Start from the second column to handle 10x12 structure
            for (char c : row.toCharArray()) {
                if (Character.isDigit(c)) {
                    int emptySquares = c - '0';
                    for (int i = 0; i < emptySquares; i++) {
                        state[rowIndex * 10 + colIndex] = Piece.None;
                        colIndex++;
                    }
                } else {
                    state[rowIndex * 10 + colIndex] = pieceMap.get(c);
                    colIndex++;
                }
            }
            rowIndex++;
        }

        Board board = new Board(state);
        board.logState();
        return board;
    }

}
