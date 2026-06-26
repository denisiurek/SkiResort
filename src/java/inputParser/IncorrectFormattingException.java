package inputParser;

public class IncorrectFormattingException extends RuntimeException {
    private final int inputLineNumber;

    public IncorrectFormattingException(String message, int inputLineNumber) {
        super(message);
        this.inputLineNumber = inputLineNumber;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " in line: " + inputLineNumber;
    }
}
