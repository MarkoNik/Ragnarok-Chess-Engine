package uci.command.receive;

import app.LoggerUtil;
import uci.command.Command;

public class QuitCommand implements Command {
    @Override
    public int execute() {
        return -1;
    }
}
