package ru.clevertec.knyazev.jsonparser.exception;

public class JSONParserException extends RuntimeException {

    public JSONParserException() {
        super();
    }

    public JSONParserException(String message) {
        super(message);
    }

    public JSONParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSONParserException(Throwable cause) {
        super(cause);
    }
}
