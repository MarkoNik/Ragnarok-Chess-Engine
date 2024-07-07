package engine.core.bitboard;

import app.Constants;
import app.EngineLogger;
import engine.util.BitUtils;

import static app.Constants.*;

public class Bitboard {

    public static final int PIECE_TYPES = 12;

    // white occupancies, black occupancies, both occupancies
    public static final int OCCUPANCY_TYPES = 3;

    private long[] pieces = new long[PIECE_TYPES];
    private long[] occupancies = new long[3];

    public void setPiece(int square, char piece) {
        // set the piece bitboard
        long bitboard = 1L << square;
        pieces[Constants.pieceMap.get(piece)] |= bitboard;

        // set the occupancies
        if (Constants.pieceMap.get(piece) < WHITE_PIECE_TYPES) {
            occupancies[WHITE] |= bitboard;
        }
        else {
            occupancies[BLACK] |= bitboard;
        }
        occupancies[BOTH] |= bitboard;
    }

    public void makeMove(int move) {
        // TODO
    }

    public void logBoardState() {
        char[] output = new char[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) output[i] = ' ';
        for (int i = 0; i < PIECE_TYPES; i++) {
            long tempBitboard = pieces[i];
            while (tempBitboard != 0) {
                int square = BitUtils.getLs1bIndex(tempBitboard);
                tempBitboard = BitUtils.popBit(tempBitboard, square);
                output[square] = Constants.asciiPieces[i];
            }
        }

        StringBuilder sb = new StringBuilder("\n\n");
        for (int i = 0; i < RANKS; i++) {
            for (int j = 0; j < FILES; j++) {
                if (j == 0) sb.append("   ").append(8 - i).append("   ");
                sb.append(output[FILES * i + j] == ' ' ? '.' : output[FILES * i + j]).append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n       a b c d e f g h\n");
        EngineLogger.debug(sb.toString());
    }

    public long[] getPieces() {
        return pieces;
    }

    public long[] getOccupancies() {
        return occupancies;
    }
}
