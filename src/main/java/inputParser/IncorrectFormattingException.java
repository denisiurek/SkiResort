package inputParser;

public class IncorrectFormattingException extends RuntimeException {
    private int inputLineNuber;

    public IncorrectFormattingException(String message, int inputLineNumber) {
        super(message);
        this.inputLineNuber = inputLineNumber;
    }

    public void appendInputLineNumber(int count) {
        inputLineNuber += count;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " in line: " + inputLineNuber;
    }
}
