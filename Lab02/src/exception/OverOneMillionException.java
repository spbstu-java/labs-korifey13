package exception;

// Класс исключений для случая N > 1'000'000
public class OverOneMillionException extends RuntimeException{
    public OverOneMillionException(String message) {
        super(message);
    }
}
