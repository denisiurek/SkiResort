package inputParser;

public class IncorrectFormattingException extends RuntimeException {
    private int inputLineNumber;

    public IncorrectFormattingException(String message, int inputLineNumber) {
        super(message);
        this.inputLineNumber = inputLineNumber;
    }

    public void appendInputLineNumber(int count) {
        inputLineNumber += count;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " in line: " + inputLineNumber;
    }
}
