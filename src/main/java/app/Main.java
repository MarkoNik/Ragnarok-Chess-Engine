package app;

import uci.Cli;

public class Main {
    public static void main(String[] args) {
        EngineLogger.info("Starting engine!");
        UciLogger.info("Starting UCI backend!");
        new Cli().run();
    }
}
