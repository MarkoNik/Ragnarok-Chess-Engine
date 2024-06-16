package uci.command;

import uci.Cli;

public class UciCommand implements Command {
    public int execute() {
        Cli.sendCommand("id name Ragnarok");
        Cli.sendCommand("id author MarkoNik");
        Cli.sendCommand("uciok");
        return 0;
    }
}
