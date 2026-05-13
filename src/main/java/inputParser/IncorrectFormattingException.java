package inputParser;

public class IncorrectFormattingException extends RuntimeException {
    public IncorrectFormattingException(String message, int inputLineNumber) {
        super(message + inputLineNumber);
    }
}
