package uci.command.receive;

import app.LoggerUtil;
import uci.command.Command;

public class RegisterCommand implements Command {

    @Override
    public int execute() {
        // TODO implement if needed
        LoggerUtil.warn("Register command received from GUI");
        return 0;
    }
}
