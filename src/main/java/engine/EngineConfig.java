package engine;

import app.LoggerUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class storing main.java.engine configuration
 * Stores a config map populated by "setoption name <id> [value <x>]" UCI command
 *
 * As the setoption command will only be invoked by the GUI while the engine is waiting,
 * we do not need to worry about concurrency issues.
 */
public class EngineConfig {
    private static EngineConfig engineConfig;
    // TODO make a set of available configuration parameters and their values
    private Map<String, String> configMap;

    private EngineConfig() {
        configMap = new HashMap<>();
    }
    public static EngineConfig get() {
        if (engineConfig == null) {
            engineConfig = new EngineConfig();
        }
        return engineConfig;
    }
    public String getOption(String name) {
        if (!configMap.containsKey(name)) {
            LoggerUtil.warn("Option with the name: " + name + " is requested but has not been set.");
        }
        return configMap.get(name);
    }
    public void setOption(String name, String value) {
        configMap.put(name, value);
    }
}
