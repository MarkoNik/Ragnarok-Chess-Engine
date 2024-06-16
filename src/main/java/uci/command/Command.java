package uci.command;

import engine.EngineState;

public interface Command {
    /**
     * Interface to represent a command received from UCI GUI.
     * Every command needs to have access to the engine state object
     * in order to read it or modify it accordingly.
     *
     * @param engineState
     * @return command status
     */
    int execute(EngineState engineState);
}
