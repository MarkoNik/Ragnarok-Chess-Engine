package uci.command.receive;

import uci.command.Command;

import static uci.Cli.cliOutput;

public class UciCommand implements Command {
    public int execute() {
        cliOutput.println("id name Ragnarok");
        cliOutput.println("id author MarkoNik");
        cliOutput.println("uciok");
        return 0;
    }
}
