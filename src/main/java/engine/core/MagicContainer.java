package engine.core;

import java.io.Serializable;

public class MagicContainer implements Serializable {
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
