package edu.netcracker.messenger.model.user.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserAlreadyExistsException extends RuntimeException {
    @Getter private final String message = "User you are trying to create is already exists";
    @Getter private final List<String> errors = new ArrayList<>();

    public UserAlreadyExistsException(Map<String, String> conflicts) {
        for (Map.Entry<String, String> error : conflicts.entrySet()) {
            errors.add(String.format("User with %s %s already exists", error.getKey(), error.getValue()));
        }
    }
}
