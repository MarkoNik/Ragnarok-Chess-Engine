package main.java.uci.command.receive;

import main.java.uci.command.Command;

public class QuitCommand implements Command {
    @Override
    public int execute() {
        return -1;
    }
}