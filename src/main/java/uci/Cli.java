package uci;

import app.UciLogger;
import uci.command.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Cli implements Runnable {
    private static BufferedReader cliInput = new BufferedReader(new InputStreamReader(System.in));
    private static PrintWriter cliOutput = new PrintWriter(new OutputStreamWriter(System.out), true);

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
        else if (line.startsWith("setoption")) {
            command = new SetOptionCommand(line);
        }
        else if (line.startsWith("register")) {
            command = new RegisterCommand();
        }
        else if (line.startsWith("position")) {
            // Handle position command
        }
        else if (line.startsWith("go")) {
            // Handle go command to start searching for the best move
        }
        else if (line.startsWith("quit")) {
            command = new QuitCommand();
        }

        return command.execute();
    }

    public static void sendCommand(String command) {
        UciLogger.info("Sending command to GUI: " + command);
        cliOutput.println(command);
    }
}
