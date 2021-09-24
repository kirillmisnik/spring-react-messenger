package edu.netcracker.messenger.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.netcracker.messenger.model.chat.Chat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"id", "account_type", "chats", "account_creation_date", "last_login_date", "last_online_date"})
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @Getter private Long id;

    @Column(name = "username")
    @Getter @Setter private String username;

    @Column(name = "password")
    @Getter @Setter private String password;

    @Column(name = "first_name")
    @Getter @Setter private String firstName;

    @Column(name = "last_name")
    @Getter @Setter private String lastName;

    @Column(name = "bio")
    @Getter @Setter private String bio;

    @Column(name = "phone_number")
    @Getter @Setter private String phoneNumber;

    @Column(name = "email")
    @Getter @Setter private String email;

    @Column(name = "profile_picture_id")
    @Getter @Setter private Long profilePictureId;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    @Getter @Setter private AccountType accountType = AccountType.USER;

    @Column(name = "account_creation_date")
    @Getter private final LocalDateTime accountCreationDate = LocalDateTime.now();

    @Column(name = "last_login_date")
    @Getter @Setter private LocalDateTime lastLoginDate;

    @Column(name = "last_online_date")
    @Getter @Setter private LocalDateTime lastOnlineDate;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "chat_members",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "chat_id") }
    )
    @Getter private List<Chat> chats;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !getAccountType().equals(AccountType.BLOCKED);
    }

    @Override
    public boolean isEnabled() {
        return !getAccountType().equals(AccountType.BLOCKED);
    }

    public boolean isAdmin() {
        return getAccountType().equals(AccountType.ADMIN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(getAccountType().name());
        return Collections.singletonList(authority);
    }
}
