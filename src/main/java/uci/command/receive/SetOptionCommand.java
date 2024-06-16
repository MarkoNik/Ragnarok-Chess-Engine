package uci.command.receive;

import engine.EngineConfig;
import uci.command.Command;

public class SetOptionCommand implements Command {
    String name, value;
    public SetOptionCommand(String params) {
        String[] split = params.trim().split("value");
        value = split[1].trim();
        String[] split2 = split[0].trim().split("name");
        name = split2[1].trim();
    }

    @Override
    public int execute() {
        EngineConfig.get().setOption(name, value);
        return 0;
    }
}
