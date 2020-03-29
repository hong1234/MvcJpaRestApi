package com.hong.demo.exceptions;

public class BookDeletionException extends RuntimeException 
{
    private static final long serialVersionUID = 1L;

    public BookDeletionException() {
        this("Book can't be deleted");
    }

    public BookDeletionException(String message) {
        this(message, null);
    }

    public BookDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
