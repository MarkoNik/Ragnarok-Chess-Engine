package uci.command;

import uci.Cli;

public class IsReadyCommand implements Command {
    @Override
    public int execute() {
        Cli.sendCommand("readyok");
        return 0;
    }
}
