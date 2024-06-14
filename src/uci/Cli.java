package uci;

import uci.command.Command;
import uci.command.receive.NullCommand;
import uci.command.receive.QuitCommand;
import uci.command.receive.UciCommand;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Cli implements Runnable {
    static BufferedReader cliInput = new BufferedReader(new InputStreamReader(System.in));
    public static PrintWriter cliOutput = new PrintWriter(new OutputStreamWriter(System.out), true);
    public void run() {
        try {
            String line;
            while ((line = cliInput.readLine()) != null) {
                int result = handleCommand(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int handleCommand(String line) {
        Command command = new NullCommand();
        if (line.equals("uci")) {
            command = new UciCommand();
        } else if (line.startsWith("position")) {
            // Handle position command
        } else if (line.startsWith("go")) {
            // Handle go command to start searching for the best move
        } else if (line.equals("quit")) {
            command = new QuitCommand();
        }
        return command.execute();
    }
}
