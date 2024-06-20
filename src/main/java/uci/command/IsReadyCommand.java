package uci.command;

import engine.state.EngineState;
import uci.Cli;

public class IsReadyCommand implements Command {
    @Override
    public int execute(EngineState engineState) {
        // TODO check if engine is ready
        Cli.sendCommand("readyok");
        return 0;
    }
}
