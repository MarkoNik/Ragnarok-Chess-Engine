package uci.command;

import engine.EngineState;
import uci.Cli;

public class IsReadyCommand implements Command {
    @Override
    public int execute(EngineState engineState) {
        Cli.sendCommand("readyok");
        return 0;
    }
}
