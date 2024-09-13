package engine.core.state;

import app.EngineLogger;
import engine.util.bits.MoveEncoder;

public class TranspositionTable {
    // TODO make the entry smaller
    // 4 * 1048576 (1MB) * 24 bytes = 96MB
    private final int SIZE = 4 * 1048576;

    public final int EXACT = 0;
    public final int LOWER_BOUND = 1;
    public final int UPPER_BOUND = 2;

    public record TTEntry(long key, int bestMove, int score, int depth, int flag) {}

    private TTEntry[] table = new TTEntry[SIZE];

    public void put(long hash, int bestMove, int score, int depth, int flag) {
        int index = (int) (Long.remainderUnsigned(hash, SIZE));
//        if (table[index] != null) {
//            EngineLogger.warn("\nHash collision: " + hash + " : " + table[index].key()
//                    + "\non key: " + index
//                    + "\nwith flag: " + flag + " : " + table[index].flag()
//                    + "\nwith depth: " + depth + " : " + table[index].depth()
//                    + "\nwith score: " + score + " : " + table[index].score
//                    + "\n");
//        }
        table[index] = new TTEntry(hash, bestMove, score, depth, flag);
    }

    public TTEntry get(long hash) {
        int index = (int) (Long.remainderUnsigned(hash, SIZE));
        TTEntry entry = table[index];
        if (entry != null && entry.key == hash) {
            return entry;
        }
        return null;
    }

    public void logTTEntry(long hash) {
        int index = (int) (Long.remainderUnsigned(hash, SIZE));
        TTEntry entry = table[index];
        if (entry != null && entry.key == hash) {
            StringBuilder sb = new StringBuilder();
            sb.append("score: ").append(entry.score());
            sb.append(" depth: ").append(entry.depth());
            EngineLogger.debug(sb.append("\n").toString());
            MoveEncoder.logMove(entry.bestMove());
        }
        else {
            EngineLogger.debug("Hash not found in TT.");
        }
    }

    public void clear() {
        table = new TTEntry[SIZE];
    }
}
