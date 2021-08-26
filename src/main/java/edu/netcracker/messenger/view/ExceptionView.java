package edu.netcracker.messenger.view;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ExceptionView {
    @Getter private final LocalDateTime timestamp = LocalDateTime.now();

    @Getter private final HttpStatus status;

    @Getter private final String message;

    @Getter private final List<String> errors;

    public ExceptionView(String message, List<String> errors, HttpStatus status) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
}
