package engine;

import app.UciLogger;
import uci.command.GoCommandWrapper;

import java.util.Map;

public class EngineState {
    private GameState gameState;

    // TODO make a set of available configuration parameters and their values
    private Map<String, String> configMap;

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    public void goSearch(GoCommandWrapper goCommandWrapper) {

    }

    public String getConfigOption(String name) {
        if (!configMap.containsKey(name)) {
            UciLogger.warn("Option with the name: " + name + " is requested but has not been set.");
        }
        return configMap.get(name);
    }
    public void setConfigOption(String name, String value) {
        configMap.put(name, value);
    }
}
