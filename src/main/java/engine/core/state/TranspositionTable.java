package engine.core.state;

public class TranspositionTable {
    // 4 * 1048576 (1MB) * 24 bytes = 96MB
    private final int SIZE = 4 * 1048576;

    public final int EXACT_FLAG = 0;
    public final int ALPHA_FLAG = 1;
    public final int BETA_FLAG = 2;

    public record TTEntry(long key, int bestMove, int score, int depth, int flag) {}

    private final TTEntry[] table = new TTEntry[SIZE];

    public void put(long hash, int bestMove, int score, int depth, int flag) {
        int index = (int) (Long.remainderUnsigned(hash, SIZE));
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
}
