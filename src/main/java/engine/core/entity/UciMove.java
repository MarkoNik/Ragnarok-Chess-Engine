package engine.core.entity;

import app.EngineLogger;

public class UciMove {
    public int from, to;
    public int potentialCastlesFlag;
    public int potentialDoublePush;
    public int promotionPiece;

    public UciMove(int from, int to, int potentialCastlesFlag, int potentialDoublePush, int promotionPiece) {
        this.from = from;
        this.to = to;
        this.potentialDoublePush = potentialDoublePush;
        this.potentialCastlesFlag = potentialCastlesFlag;
        this.promotionPiece = promotionPiece;
        EngineLogger.debug("Created new Uci move: " + this);
    }

    @Override
    public String toString() {
        return "UciMove{" +
                "from=" + from +
                ", to=" + to +
                ", castlesFlag=" + potentialCastlesFlag +
                ", potentialDoublePush=" + potentialDoublePush +
                ", promotionPiece=" + promotionPiece +
                '}';
    }
}
