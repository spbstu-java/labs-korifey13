package exception;

// Класс исключений для случая, когда файл не существует/нет доступа к файлу
public class FileNotFoundOrBusyException extends RuntimeException{
    public FileNotFoundOrBusyException(String message) {
        super(message);
    }
}
