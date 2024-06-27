package app;

import engine.core.Bitboard;
import engine.state.EngineState;
import uci.Cli;

public class Main {
    public static void main(String[] args) {
        Bitboard bitboard = new Bitboard();
        bitboard.fillAttackTables();

        EngineLogger.info("Starting engine!");
        UciLogger.info("Starting UCI backend!");
        EngineState engineState = new EngineState();
        new Cli(engineState).run();
    }
}
