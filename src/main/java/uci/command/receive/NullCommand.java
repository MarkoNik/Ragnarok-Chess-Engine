package uci.command.receive;

import uci.command.Command;

public class NullCommand implements Command {
    @Override
    public int execute() {
        return 0;
    }
}
