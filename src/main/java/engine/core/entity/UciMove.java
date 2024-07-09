package engine.core.entity;

public class UciMove {
    public int from, to;
    public boolean castlesFlag;
    public boolean potentialDoublePush;
    public int promotionPiece;

    public UciMove(int from, int to, boolean castlesFlag, boolean potentialDoublePush, int promotionPiece) {
        this.from = from;
        this.to = to;
        this.potentialDoublePush = potentialDoublePush;
        this.castlesFlag = castlesFlag;
        this.promotionPiece = promotionPiece;
    }
}
