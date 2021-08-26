package edu.netcracker.messenger.view.user;

import edu.netcracker.messenger.model.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * User info available to everyone.
 */
public class UserPublicView implements UserView {

    @Getter private final Long id;

    @Getter private final String username;

    @Getter private final String firstName;

    @Getter private final String lastName;

    @Getter private final String bio;

    @Getter private final LocalDateTime lastOnlineDate;

    public UserPublicView(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.bio = user.getBio();
        this.lastOnlineDate = user.getLastOnlineDate();
    }
}