package main.java.uci.command.receive;

import main.java.uci.command.Command;

public class DebugCommand implements Command {
    private boolean on;

    public DebugCommand(String params) {
        String[] split = params.trim().split("\\s++");
        if (split[1].equals("on") || split[1].equals("off")) {
            on = split[1].equals("on");
        }
        // TODO handle error if not on nor off
    }

    @Override
    public int execute() {
        // TODO
        return 0;
    }
}
