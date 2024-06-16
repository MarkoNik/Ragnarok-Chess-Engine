package uci.command;

import engine.EngineState;
import uci.Cli;

public class UciCommand implements Command {
    public int execute(EngineState engineState) {
        Cli.sendCommand("id name Ragnarok");
        Cli.sendCommand("id author MarkoNik");
        Cli.sendCommand("uciok");
        return 0;
    }
}
