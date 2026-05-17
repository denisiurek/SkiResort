package simulation;

public enum LogLevel {
    NONE(0),
    INFO(1),
    PRODUCTION(2),
    DEBUG(3);

    private final int value;

    LogLevel(int value) {this.value = value;}

    public boolean allows(LogLevel other) {
        return this.value >= other.value;
    }
}
