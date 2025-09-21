package com.entreprise.gestion.exception;

public class MyException extends Exception {

    // Constructeur par défaut
    public MyException() {
        super();
    }

    // Constructeur avec message
    public MyException(String message) {
        super(message);
    }

    // Constructeur avec message et cause
    public MyException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur avec cause
    public MyException(Throwable cause) {
        super(cause);
    }
}
