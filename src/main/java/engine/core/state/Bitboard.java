package engine.core.state;

import app.Constants;
import app.EngineLogger;
import engine.core.entity.Piece;
import engine.core.entity.UciMove;
import engine.util.bits.BitUtils;
import engine.util.bits.MoveEncoder;

import java.util.Arrays;

import static app.Constants.*;

public class Bitboard {
    private long[] pieces = new long[PIECE_TYPES];
    private long[] occupancies = new long[OCCUPANCY_TYPES];

    /**
     * 0001 - white kingside castles<br>
     * 0010 - white queenside castles<br>
     * 0100 - black kingside castles<br>
     * 1000 - black queenside castles<br>
     */
    private byte castlesFlags = 0;
    private int enPassantSquare = 0;

    private long[] piecesBackup = new long[PIECE_TYPES];
    private long[] occupanciesBackup = new long[OCCUPANCY_TYPES];
    private byte castlesFlagsBackup = 0;
    private int enPassantSquareBackup = 0;

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

    public void makeMove(int move, boolean isWhiteTurn, boolean capturesOnly) {
        if (!capturesOnly) {
            int from = MoveEncoder.extractFrom(move);
            int to = MoveEncoder.extractTo(move);
            int piece = MoveEncoder.extractPiece(move);
            int promotionPiece = MoveEncoder.extractPromotionPiece(move);
            int doublePushFlag = MoveEncoder.extractDoublePushFlag(move);
            int castlesFlag = MoveEncoder.extractCastlesFlag(move);
            int enPassantFlag = MoveEncoder.extractEnPassantFlag(move);
            int captureFlag = MoveEncoder.extractCaptureFlag(move);
//            if (!BitUtils.getBit(pieces[piece], from)) {
//                EngineLogger.error("Tried to move inexistent piece: " + piece + " from: " + from + " to " + to);
//            }
            pieces[piece] = BitUtils.popBit(pieces[piece], from);
            pieces[piece] = BitUtils.setBit(pieces[piece], to);
            if (isWhiteTurn) {
                occupancies[WHITE] = BitUtils.popBit(occupancies[WHITE], from);
                occupancies[WHITE] = BitUtils.setBit(occupancies[WHITE], to);
            } else {
                occupancies[BLACK] = BitUtils.popBit(occupancies[BLACK], from);
                occupancies[BLACK] = BitUtils.setBit(occupancies[BLACK], to);
            }

            if (captureFlag != 0) {
                if (isWhiteTurn) {
                    // iterate over all black piece types
                    for (int i = WHITE_PIECE_TYPES; i < PIECE_TYPES; i++) {
                        if (BitUtils.getBit(pieces[i], to)) {
                            pieces[i] = BitUtils.popBit(pieces[i], to);
                            break;
                        }
                    }
                    occupancies[BLACK] = BitUtils.popBit(occupancies[BLACK], to);
                }
                else {
                    // iterate over all white piece types
                    for (int i = 0; i < WHITE_PIECE_TYPES; i++) {
                        if (BitUtils.getBit(pieces[i], to)) {
                            pieces[i] = BitUtils.popBit(pieces[i], to);
                            break;
                        }
                    }
                    occupancies[WHITE] = BitUtils.popBit(occupancies[WHITE], to);
                }
            }

            if (promotionPiece != 0) {
                pieces[piece] = BitUtils.popBit(pieces[piece], to);
                pieces[promotionPiece] = BitUtils.setBit(pieces[promotionPiece], to);
            }

            if (doublePushFlag != 0) {
                enPassantSquare = to;
            } else {
                enPassantSquare = -1;
            }

            if (castlesFlag != 0) {
                if (to == WHITE_KINGSIDE_CASTLES_SQUARE) {
                    pieces[pieceMap.get('R')] = BitUtils.popBit(pieces[pieceMap.get('R')], WHITE_KINGSIDE_ROOK);
                    pieces[pieceMap.get('R')] = BitUtils.popBit(pieces[pieceMap.get('R')], WHITE_KINGSIDE_CASTLES_SQUARE - 1);
                    occupancies[WHITE] = BitUtils.popBit(occupancies[WHITE], WHITE_KINGSIDE_ROOK);
                    occupancies[WHITE] = BitUtils.setBit(occupancies[WHITE], WHITE_KINGSIDE_CASTLES_SQUARE - 1);
                }
                if (to == WHITE_QUEENSIDE_CASTLES_SQUARE) {
                    pieces[pieceMap.get('R')] = BitUtils.popBit(pieces[pieceMap.get('R')], WHITE_QUEENSIDE_ROOK);
                    pieces[pieceMap.get('R')] = BitUtils.popBit(pieces[pieceMap.get('R')], WHITE_QUEENSIDE_CASTLES_SQUARE + 1);
                    occupancies[WHITE] = BitUtils.popBit(occupancies[WHITE], WHITE_QUEENSIDE_ROOK);
                    occupancies[WHITE] = BitUtils.setBit(occupancies[WHITE], WHITE_QUEENSIDE_CASTLES_SQUARE + 1);
                }
                if (to == BLACK_KINGSIDE_CASTLES_SQUARE) {
                    pieces[pieceMap.get('r')] = BitUtils.popBit(pieces[pieceMap.get('r')], BLACK_KINGSIDE_ROOK);
                    pieces[pieceMap.get('r')] = BitUtils.popBit(pieces[pieceMap.get('r')], BLACK_KINGSIDE_CASTLES_SQUARE - 1);
                    occupancies[BLACK] = BitUtils.popBit(occupancies[BLACK], BLACK_KINGSIDE_ROOK);
                    occupancies[BLACK] = BitUtils.setBit(occupancies[BLACK], BLACK_KINGSIDE_CASTLES_SQUARE - 1);
                }
                if (to == BLACK_QUEENSIDE_CASTLES_SQUARE) {
                    pieces[pieceMap.get('r')] = BitUtils.popBit(pieces[pieceMap.get('r')], BLACK_QUEENSIDE_ROOK);
                    pieces[pieceMap.get('r')] = BitUtils.popBit(pieces[pieceMap.get('r')], BLACK_QUEENSIDE_CASTLES_SQUARE + 1);
                    occupancies[BLACK] = BitUtils.popBit(occupancies[BLACK], BLACK_QUEENSIDE_ROOK);
                    occupancies[BLACK] = BitUtils.setBit(occupancies[BLACK], BLACK_QUEENSIDE_CASTLES_SQUARE + 1);
                }
            }

//            if (enPassantFlag != 0) {
//                if (isWhiteTurn) {
//                    pieces[pieceMap.get('p')] = BitUtils.popBit(pieces[pieceMap.get('p')], enPassantSquare);
//                }
//                else {
//                    pieces[pieceMap.get('P')] = BitUtils.popBit(pieces[pieceMap.get('P')], enPassantSquare);
//                }
//                enPassantSquare = -1;
//            }
            occupancies[BOTH] = occupancies[WHITE] | occupancies[BLACK];
        }
    }

    public void makeUciMove(UciMove uciMove, boolean isWhiteTurn) {
        int from = uciMove.from;
        int to = uciMove.to;
        int piece = -1;
        for (int i = 0; i < PIECE_TYPES; i++) {
            if ((pieces[i] & (1L << from)) != 0) {
                piece = i;
                break;
            }
        }

        int promotionPiece = uciMove.promotionPiece;
        if (isWhiteTurn) {
            if (promotionPiece == Piece.Queen) promotionPiece = Piece.WhiteQueen;
            else if (promotionPiece == Piece.Rook) promotionPiece = Piece.WhiteRook;
            else if (promotionPiece == Piece.Bishop) promotionPiece = Piece.WhiteBishop;
            else if (promotionPiece == Piece.Knight) promotionPiece = Piece.WhiteKnight;
        }
        else {
            if (promotionPiece == Piece.Queen) promotionPiece = Piece.BlackQueen;
            else if (promotionPiece == Piece.Rook) promotionPiece = Piece.BlackRook;
            else if (promotionPiece == Piece.Bishop) promotionPiece = Piece.BlackBishop;
            else if (promotionPiece == Piece.Knight) promotionPiece = Piece.BlackKnight;
        }

        int doublePushFlag = 0;
        if (uciMove.potentialDoublePush && isWhiteTurn && piece == Piece.WhitePawn
                || uciMove.potentialDoublePush && !isWhiteTurn && piece == Piece.BlackPawn) {
            doublePushFlag = 1;
        }

        int castlesFlag = uciMove.castlesFlag ? 1 : 0;
        int captureFlag = 0;
        if ((isWhiteTurn && (occupancies[BLACK] & (1L << to)) != 0)
                || (!isWhiteTurn && (occupancies[WHITE] & (1L << to)) != 0)) {
            captureFlag = 1;
        }

        int enPassantFlag = 0;
        int move = MoveEncoder.encodeMove(from, to, piece, promotionPiece, doublePushFlag, castlesFlag, enPassantFlag, captureFlag);
        makeMove(move, isWhiteTurn, false);
    }

    public void backupState() {
        piecesBackup = pieces.clone();
        occupanciesBackup = occupancies.clone();
        castlesFlagsBackup = castlesFlags;
        enPassantSquareBackup = enPassantSquare;
    }

    public void restoreState() {
        pieces = piecesBackup;
        occupancies = occupanciesBackup;
        castlesFlags = castlesFlagsBackup;
        enPassantSquare = enPassantSquareBackup;
    }

    public void logBoardState() {
        char[] output = new char[BOARD_SIZE];
        Arrays.fill(output, ' ');
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
        logBitboards();
    }

    public void logBitboards() {
        for (int i = 0; i < PIECE_TYPES; i++) {
            EngineLogger.debug("Piece bitboard: " + Piece.pieceCodeToPiece[i]);
            BitUtils.logBitboard(pieces[i]);
        }
    }

    public long[] getPieces() {
        return pieces;
    }

    public long[] getOccupancies() {
        return occupancies;
    }

    public byte getCastlesFlags() {
        return castlesFlags;
    }

    public int getEnPassantSquare() {
        return enPassantSquare;
    }

    public void setCastlesFlags(byte castlesFlags) {
        this.castlesFlags = castlesFlags;
    }

    public void setEnPassantSquare(int enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }
}
