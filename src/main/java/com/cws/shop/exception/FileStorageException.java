package com.cws.shop.exception;

public class FileStorageException extends RuntimeException{

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileStorageException(String fileNameIsInvalid) {
        super(fileNameIsInvalid);
    }
}
