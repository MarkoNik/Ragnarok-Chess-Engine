package app;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final int ROWS = 8;
    public static final int COLS = 8;
    public static final int BOARD_SIZE = ROWS * COLS;
    public static final String INITIAL_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static Map<Character, Integer> pieceMap = new HashMap<>();
    public static char[] asciiPieces = {
            'P',
            'N',
            'B',
            'R',
            'Q',
            'K',
            'p',
            'n',
            'b',
            'r',
            'q',
            'k'
    };
    public static char[] unicodePieces = {
            '♙',
            '♘',
            '♗',
            '♖',
            '♕',
            '♔',
            '♟',
            '♞',
            '♝',
            '♜',
            '♛',
            '♚'
    };

    static {
        Constants.pieceMap.put('P', 0);
        Constants.pieceMap.put('N', 1);
        Constants.pieceMap.put('B', 2);
        Constants.pieceMap.put('R', 3);
        Constants.pieceMap.put('Q', 4);
        Constants.pieceMap.put('K', 5);
        Constants.pieceMap.put('p', 6);
        Constants.pieceMap.put('n', 7);
        Constants.pieceMap.put('b', 8);
        Constants.pieceMap.put('r', 9);
        Constants.pieceMap.put('q', 10);
        Constants.pieceMap.put('k', 11);
    }
}
