package edu.netcracker.messenger.controller.api.user;

import edu.netcracker.messenger.model.user.User;
import edu.netcracker.messenger.model.user.UserRepository;
import edu.netcracker.messenger.model.user.exception.UserAlreadyExistsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

/**
 * Controller for handling requests related to user registration.
 */
@Controller
@RequestMapping("api/registration")
public class RegistrationApiController {
    private final UserRepository repository;

    RegistrationApiController(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Registries user.
     * @return user id
     * @throws UserAlreadyExistsException user already exists
     */
    @PostMapping
    public @ResponseBody
    Long registerUser(@RequestBody User newUser) throws UserAlreadyExistsException {
        Map<String, String> errors = new TreeMap<>();
        if (repository.getByUsername(newUser.getUsername()) != null) {
            errors.put("username", newUser.getUsername());
        }
        if (repository.getByPhoneNumber(newUser.getPhoneNumber()) != null) {
            errors.put("phone number", newUser.getPhoneNumber());
        }
        if (repository.getByEmail(newUser.getEmail()) != null) {
            errors.put("email", newUser.getEmail());
        }
        if (!errors.isEmpty()) {
            throw new UserAlreadyExistsException(errors);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        repository.save(newUser);
        return newUser.getId();
    }
}