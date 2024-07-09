package engine.core.entity;

import app.EngineLogger;

public class UciMove {
    public int from, to;
    public int castlesFlag;
    public int potentialDoublePush;
    public int promotionPiece;

    public UciMove(int from, int to, int castlesFlag, int potentialDoublePush, int promotionPiece) {
        this.from = from;
        this.to = to;
        this.potentialDoublePush = potentialDoublePush;
        this.castlesFlag = castlesFlag;
        this.promotionPiece = promotionPiece;
        EngineLogger.debug("Created new Uci move: " + this);
    }

    @Override
    public String toString() {
        return "UciMove{" +
                "from=" + from +
                ", to=" + to +
                ", castlesFlag=" + castlesFlag +
                ", potentialDoublePush=" + potentialDoublePush +
                ", promotionPiece=" + promotionPiece +
                '}';
    }
}
