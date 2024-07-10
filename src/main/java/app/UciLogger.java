package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static app.Constants.USE_LOGGER;

public class UciLogger {
    private static final Logger logger = LoggerFactory.getLogger(UciLogger.class);
    public static void debug(String message) {
        if (USE_LOGGER) {
            logger.debug(message);
        }
    }
    public static void info(String message) {
        if (USE_LOGGER) {
            logger.info(message);
        }
    }
    public static void warn(String message) {
        if (USE_LOGGER) {
            logger.warn(message);
        }
    }
    public static void error(String message) {
        if (USE_LOGGER) {
            logger.error(message);
        }
    }
}
