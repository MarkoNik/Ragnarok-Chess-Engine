package uci.command.receive;

import uci.command.Command;

import static uci.Cli.cliOutput;

public class IsReadyCommand implements Command {
    @Override
    public int execute() {
        cliOutput.println("readyok");
        return 0;
    }
}
