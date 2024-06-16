package app;

import engine.EngineState;
import uci.Cli;

public class Main {
    public static void main(String[] args) {
        EngineLogger.info("Starting engine!");
        UciLogger.info("Starting UCI backend!");
        EngineState engineState = new EngineState();
        new Cli(engineState).run();
    }
}
