package engine.util;

import app.EngineLogger;
import engine.core.entity.Piece;
import engine.core.state.Bitboard;
import engine.util.bits.BitUtils;

import java.util.Random;

import static app.Constants.*;
import static engine.core.entity.Piece.PIECE_TYPES;

public class Zobrist {
    private static final long seed = 0;
    public static final long[][] pieceKeys = new long[PIECE_TYPES][BOARD_SIZE];
    public static final long[] enPassantKeys = new long[BOARD_SIZE];
    public static final long[] castlesFlagsKeys = new long[ALL_CASTLES_MASK + 1];
    public static final long sidesKey;

    static {
        Random random = new Random(seed);
        for (int i = 0; i < PIECE_TYPES; i++) {
            for (int square = 0; square < BOARD_SIZE; square++) {
                pieceKeys[i][square] = random.nextLong();
            }
        }

        for (int square = 0; square < BOARD_SIZE; square++) {
            enPassantKeys[square] = random.nextLong();
        }

        for (int i = 0; i < ALL_CASTLES_MASK + 1; i++) {
            castlesFlagsKeys[i] = random.nextLong();
        }

        sidesKey = random.nextLong();
    }

    public static long generateHash(Bitboard bitboard, boolean isWhiteTurn) {
        long hash = 0L;

        long[] pieces = bitboard.getPieces();
        for (int i = 0; i < PIECE_TYPES; i++) {
            long tempPiece = pieces[i];
            while (tempPiece != 0) {
                int square = BitUtils.getLs1bIndex(tempPiece);
                hash ^= pieceKeys[i][square];
                tempPiece = BitUtils.popBit(tempPiece, square);
            }
        }

        if (bitboard.getEnPassantSquare() != -1) {
            hash ^= enPassantKeys[bitboard.getEnPassantSquare()];
        }

        hash ^= castlesFlagsKeys[bitboard.getCastlesFlags()];

        if (!isWhiteTurn) {
            hash ^= sidesKey;
        }

        return hash;
    }

    public static void logError(long hash1, long hash2) {
        long diff = hash1 ^ hash2;
        for (int i = 0; i < PIECE_TYPES; i++) {
            for (int square = 0; square < BOARD_SIZE; square++) {
                if (pieceKeys[i][square] == diff) {
                    EngineLogger.error("Error on piece: " + Piece.pieceCodeToPiece[i] + "at square: " + square);
                }
            }
        }

        for (int square = 0; square < BOARD_SIZE; square++) {
            if (enPassantKeys[square] == diff) {
                EngineLogger.error("Error en passant on square: " + square);
            }
        }

        for (int i = 0; i < ALL_CASTLES_MASK + 1; i++) {
            if (castlesFlagsKeys[i] == diff) {
                EngineLogger.error("Error en castles flags: " + i);
            }
        }

        if (sidesKey == diff) {
            EngineLogger.error("Error en sides key: " + sidesKey);
        }
    }
}
