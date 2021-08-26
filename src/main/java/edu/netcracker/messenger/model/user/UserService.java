package edu.netcracker.messenger.model.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.getByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", s));
        }
        if (!user.isAccountNonLocked()) {
            throw new RuntimeException(String.format("User %s is blocked", s));
        }
        return user;
    }
}
