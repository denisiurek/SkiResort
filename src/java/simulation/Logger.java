package simulation;

import simulation.events.Event;

import java.io.OutputStream;
import java.io.PrintStream;

public class Logger {
    final PrintStream printStream;
    final LogLevel setVerbosity;

    public Logger(OutputStream stream, LogLevel verbosity) {
        this.printStream = new PrintStream(stream);
        this.setVerbosity = verbosity;
    }

    private void outputLog(String entry) {
        printStream.println(entry);
    }

    public void log(Event event) {
        if (setVerbosity.allows(event.getLogLevel())) {
            outputLog(event.toString());
        }
    }
    public void log(String entry, LogLevel logLevel) {
        if (setVerbosity.allows(logLevel)) {
            outputLog(entry);
        }
    }
}
