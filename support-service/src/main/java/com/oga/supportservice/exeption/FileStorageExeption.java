package com.oga.supportservice.exeption;

public class FileStorageExeption extends RuntimeException {

    public FileStorageExeption(String message) {
        super(message);
    }

    public FileStorageExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
