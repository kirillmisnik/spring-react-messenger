package edu.netcracker.messenger.model.chat.exception;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(Long id) {
        super("Could not find chat with id: " + id);
    }
}
