package engine.core;

public class MagicContainer {
    long magicNumber;
    long[] attackMap;

    public MagicContainer(long magicNumber, long[] magicMap) {
        this.magicNumber = magicNumber;
        this.attackMap = magicMap;
    }

    public long getMagicNumber() {
        return magicNumber;
    }
}
