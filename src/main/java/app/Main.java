package app;

import engine.core.state.EngineState;
import uci.Cli;

public class Main {
    public static void main(String[] args) {
        try {
            EngineLogger.info("Starting engine!");
            UciLogger.info("Starting UCI backend!");
            EngineState engineState = new EngineState();
            new Cli(engineState).run();
        } catch (Exception e) {
            EngineLogger.error(e.getMessage());
        }
    }
}
