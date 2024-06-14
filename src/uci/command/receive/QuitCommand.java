package uci.command.receive;

import uci.command.Command;

public class QuitCommand implements Command {
    @Override
    public int execute() {
        return -1;
    }
}
