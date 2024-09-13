package uci;

import app.UciLogger;
import engine.core.state.EngineState;
import uci.command.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Cli implements Runnable {
    private static final BufferedReader cliInput = new BufferedReader(new InputStreamReader(System.in));
    private static final PrintWriter cliOutput = new PrintWriter(new OutputStreamWriter(System.out), true);
    private final EngineState engineState;

    public Cli(EngineState engineState) {
        this.engineState = engineState;
    }

    public void run() {
        try {
            String line;
            while ((line = cliInput.readLine()) != null) {
                UciLogger.info("Received command from GUI: " + line);
                int result = handleCommand(line);
                if (result == -1) {
                    UciLogger.info("Stopping UCI backend!");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int handleCommand(String line) {
        Command command = new NullCommand();

        if (line.startsWith("uci")) {
            command = new UciCommand();
        }
        else if (line.startsWith("debug")) {
            command = new DebugCommand(line);
        }
        else if (line.startsWith("isready")) {
            command = new IsReadyCommand();
        }
        else if (line.startsWith("setoption")) {
            command = new SetOptionCommand(line);
        }
        else if (line.startsWith("register")) {
            command = new RegisterCommand();
        }
        else if (line.startsWith("ucinewgame")) {
            command = new UciNewGameCommand();
        }
        else if (line.startsWith("position")) {
            command = new PositionCommand(line);
        }
        else if (line.startsWith("go")) {
            command = new GoCommand(line);
        }
        else if (line.startsWith("stop")) {
            command = new StopCommand();
        }
        else if (line.startsWith("ponderhit")) {
            command = new PonderHitCommand();
        }
        else if (line.startsWith("quit")) {
            command = new QuitCommand();
        }
        else if (line.startsWith("make")) {
            command = new MakeMoveCommand(line);
        }
        else if (line.startsWith("unmake")) {
            command = new UnmakeMoveCommand();
        }
        return command.execute(engineState);
    }

    public static void sendCommand(String command) {
        UciLogger.info("Sending command to GUI: " + command);
        cliOutput.println(command);
    }
}
