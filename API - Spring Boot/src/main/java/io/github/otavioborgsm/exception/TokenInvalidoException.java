package io.github.otavioborgsm.exception;

public class TokenInvalidoException extends Exception {

    public TokenInvalidoException() {
        super();
    }


    public TokenInvalidoException(String message) {
        super(message);
    }


    public TokenInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
