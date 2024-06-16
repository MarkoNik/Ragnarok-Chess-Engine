package uci.command.receive;

import app.LoggerUtil;
import engine.EngineConfig;
import uci.command.Command;

public class SetOptionCommand implements Command {
    // default values in case value is absent or parsing goes wrong
    String name = "", value = "";
    public SetOptionCommand(String params) {
        String[] split = params.trim().split("value");
        if (split.length == 2) {
            value = split[1].trim();
        }

        String[] split2 = split[0].trim().split("name");
        if (split2.length == 2) {
            name = split2[1].trim();
        }
        else {
            LoggerUtil.error("Error parsing setoption UCI command: name not specified");
        }
    }

    @Override
    public int execute() {
        EngineConfig.get().setOption(name, value);
        return 0;
    }
}
