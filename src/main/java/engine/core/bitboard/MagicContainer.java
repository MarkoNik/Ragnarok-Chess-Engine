package engine.core.bitboard;

public class MagicContainer {
    private long magicNumber;
    private long[] attackMap;

    public MagicContainer(long magicNumber, long[] magicMap) {
        this.magicNumber = magicNumber;
        this.attackMap = magicMap;
    }

    public long getMagicNumber() {
        return magicNumber;
    }

    public long[] getAttackMap() {
        return attackMap;
    }
}
