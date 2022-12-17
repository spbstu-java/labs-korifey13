package exception;

// Класс исключений для случая деления на 0
public class DivisionByZeroException extends RuntimeException{
    public DivisionByZeroException(String message) {
        super(message);
    }
}
