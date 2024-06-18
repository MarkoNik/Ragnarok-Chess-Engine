package uci.command;

import java.util.ArrayList;
import java.util.List;

public class GoCommandWrapper {
    public List<String> searchMoves;
    public boolean ponder;
    public int wtime;
    public int btime;
    public int winc;
    public int binc;
    public int movesToGo;
    public int depth;
    public int nodes;
    public int mate;
    public int moveTime;
    public boolean infinite;

    public GoCommandWrapper() {
        searchMoves = new ArrayList<>();
        ponder = false;
        wtime = -1;
        btime = -1;
        winc = -1;
        binc = -1;
        movesToGo = -1;
        depth = -1;
        nodes = -1;
        mate = -1;
        moveTime = -1;
        infinite = false;
    }

    @Override
    public String toString() {
        return "GoCommand{" +
                "searchMoves=" + searchMoves +
                ", ponder=" + ponder +
                ", wtime=" + wtime +
                ", btime=" + btime +
                ", winc=" + winc +
                ", binc=" + binc +
                ", movesToGo=" + movesToGo +
                ", depth=" + depth +
                ", nodes=" + nodes +
                ", mate=" + mate +
                ", moveTime=" + moveTime +
                ", infinite=" + infinite +
                '}';
    }
}
