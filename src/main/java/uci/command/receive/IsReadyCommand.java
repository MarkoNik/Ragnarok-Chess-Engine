package main.java.uci.command.receive;

import main.java.uci.command.Command;

import static main.java.uci.Cli.cliOutput;

public class IsReadyCommand implements Command {
    @Override
    public int execute() {
        cliOutput.println("readyok");
        return 0;
    }
}
