package app;

public class Constants {
    public static final int INF = 1 << 30;
    public static final int RANKS = 8;
    public static final int FILES = 8;
    public static final int BOARD_SIZE = RANKS * FILES;
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    public static final int BOTH = 2;
    public static final long RANK_5 = 0x00000000FF000000L;
    public static final long RANK_4 = 0x000000FF00000000L;
    public static final long RANK_8_END_SQUARE = 7;
    public static final long RANK_1_START_SQUARE = 56;
    public static final long RANK_7_START_SQUARE = 8;
    public static final long RANK_7_END_SQUARE = 15;
    public static final long RANK_2_START_SQUARE = 48;
    public static final long RANK_2_END_SQUARE = 55;
    public static final int WHITE_KINGSIDE_CASTLES_MASK = 1;
    public static final int WHITE_QUEENSIDE_CASTLES_MASK = 2;
    public static final int BLACK_KINGSIDE_CASTLES_MASK = 4;
    public static final int BLACK_QUEENSIDE_CASTLES_MASK = 8;
    public static final int ALL_CASTLES_MASK = 15;
    public static final int WHITE_KINGSIDE_CASTLES_SQUARE = 62;
    public static final int WHITE_QUEENSIDE_CASTLES_SQUARE = 58;
    public static final int BLACK_KINGSIDE_CASTLES_SQUARE = 6;
    public static final int BLACK_QUEENSIDE_CASTLES_SQUARE = 2;
    public static final int WHITE_KINGSIDE_ROOK_POSITION = 63;
    public static final int WHITE_QUEENSIDE_ROOK_POSITION = 56;
    public static final int BLACK_KINGSIDE_ROOK_POSITION = 7;
    public static final int BLACK_QUEENSIDE_ROOK_POSITION = 0;
    public static final int WHITE_KING_POSITION = 60;
    public static final int BLACK_KING_POSITION = 4;
    public static final int ILLEGAL = -1;

    // https://www.stmintz.com/ccc/index.php?id=424966
    public static final int MAX_LEGAL_MOVES = 220;

    public static final String INITIAL_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    // white occupancies, black occupancies, both occupancies
    public static final int OCCUPANCY_TYPES = 3;

    public static final boolean USE_LOGGER = true;
}
