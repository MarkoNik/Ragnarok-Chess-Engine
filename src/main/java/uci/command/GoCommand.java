package uci.command;

import engine.state.EngineState;
import uci.Cli;

import java.util.Arrays;
import java.util.List;

public class GoCommand implements Command {
    private GoCommandWrapper goCommandWrapper = new GoCommandWrapper();
    private List<String> keywords;

    // This command requires more complex parsing than other ones
    public GoCommand(String params) {
        // Set all possible keywords because searchmoves can have multiple space separated params
        keywords = Arrays.asList("searchmoves", "ponder", "wtime", "btime", "winc", "binc", "movestogo",
                "depth", "nodes", "mate", "movetime", "infinite");

        String[] tokens = params.split("\\s+");
        int i = 0;

        while (i < tokens.length) {
            switch (tokens[i]) {
                case "searchmoves":
                    i++;
                    while (i < tokens.length && !keywords.contains(tokens[i])) {
                        goCommandWrapper.searchMoves.add(tokens[i]);
                        i++;
                    }
                    break;
                case "ponder":
                    goCommandWrapper.ponder = true;
                    i++;
                    break;
                case "wtime":
                    goCommandWrapper.wtime = Integer.parseInt(tokens[++i]);
                    i++;
                    break;
                case "btime":
                    goCommandWrapper.btime = Integer.parseInt(tokens[++i]);
                    i++;
                    break;
                case "winc":
                    goCommandWrapper.winc = Integer.parseInt(tokens[++i]);
                    i++;
                    break;
                case "binc":
                    goCommandWrapper.binc = Integer.parseInt(tokens[++i]);
                    i++;
                    break;
                case "movestogo":
                    goCommandWrapper.movesToGo = Integer.parseInt(tokens[++i]);
                    i++;
                    break;
                case "depth":
                    goCommandWrapper.depth = Integer.parseInt(tokens[++i]);
                    i++;
                    break;
                case "nodes":
                    goCommandWrapper.nodes = Integer.parseInt(tokens[++i]);
                    i++;
                    break;
                case "mate":
                    goCommandWrapper.mate = Integer.parseInt(tokens[++i]);
                    i++;
                    break;
                case "movetime":
                    goCommandWrapper.moveTime = Integer.parseInt(tokens[++i]);
                    i++;
                    break;
                case "infinite":
                    goCommandWrapper.infinite = true;
                    i++;
                    break;
                default:
                    i++;
                    break;
            }
        }
    }

    @Override
    public int execute(EngineState engineState) {
        engineState.search(goCommandWrapper);
        Cli.sendCommand("bestmove d7d5");
        return 0;
    }
}
