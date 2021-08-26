package edu.netcracker.messenger.model.user.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserNotFoundException extends RuntimeException {
    @Getter private final String message = "User you are trying to find not exists";
    @Getter private List<String> errors = new ArrayList<>();

    public UserNotFoundException(List<Long> conflicts) {
        for (Long userId : conflicts) {
            errors.add(String.format("User with id: %d does not exists", userId));
        }
    }

    public UserNotFoundException(Long id) {
        errors = Collections.singletonList("Could not find user with id: " + id);
    }

    public UserNotFoundException() {
        errors = Collections.singletonList("No users found");
    }
}
