package ru.mai.lessons.rpks.exception;

public class DatabaseConnectionException extends RuntimeException {

    public DatabaseConnectionException(String message) {
        super(message);
    }

}
