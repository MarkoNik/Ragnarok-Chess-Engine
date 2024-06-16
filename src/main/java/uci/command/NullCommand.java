package uci.command;

import app.UciLogger;

public class NullCommand implements Command {
    @Override
    public int execute() {
        UciLogger.warn("Received unrecognized command.");
        return 0;
    }
}
