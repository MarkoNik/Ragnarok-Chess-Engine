package main.java.uci.command.receive;

import main.java.uci.command.Command;

import static main.java.uci.Cli.cliOutput;

public class UciCommand implements Command {
    public int execute() {
        cliOutput.println("id name Ragnarok");
        cliOutput.println("id author MarkoNik");
        cliOutput.println("uciok");
        return 0;
    }
}
