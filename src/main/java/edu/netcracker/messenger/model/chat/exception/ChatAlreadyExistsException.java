package edu.netcracker.messenger.model.chat.exception;

public class ChatAlreadyExistsException extends RuntimeException {
    public ChatAlreadyExistsException() {
        super("Personal chat already exists");
    }
}
