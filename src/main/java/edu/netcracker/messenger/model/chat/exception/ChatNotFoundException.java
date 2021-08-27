package edu.netcracker.messenger.model.chat.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ChatNotFoundException extends RuntimeException {
    @Getter private final String message = "Chat you are trying to find not exists";
    @Getter private List<String> errors = new ArrayList<>();

    public ChatNotFoundException(Long id) {
        errors.add("Could not find chat with id: " + id);
    }
}
