package edu.netcracker.messenger.controller.api.user;

import edu.netcracker.messenger.model.chat.Chat;
import edu.netcracker.messenger.model.message.Message;
import edu.netcracker.messenger.model.message.MessageRepository;
import edu.netcracker.messenger.model.user.AccountType;
import edu.netcracker.messenger.model.user.User;
import edu.netcracker.messenger.model.user.exception.UserNotFoundException;
import edu.netcracker.messenger.model.user.UserRepository;
import edu.netcracker.messenger.view.chat.ChatListView;
import edu.netcracker.messenger.view.user.UserPrivateView;
import edu.netcracker.messenger.view.user.UserPublicView;
import edu.netcracker.messenger.view.user.UserView;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling requests related to users.
 */
@Controller
@RequestMapping("api/user")
public class UserApiController {

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    UserApiController(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    /**
     * Returns a list of all registered users.
     *
     * @return list of all users
     */
    @GetMapping("/all")
    public @ResponseBody
    List<UserView> allUsers(Principal principal) {
        throwIfNoUserExists();
        List<UserView> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (!loggedInUser(principal).getId().equals(user.getId())) {
                users.add(new UserPublicView(user));
            }
        }
        return users;
    }

    /**
     * Returns user information.
     *
     * @param principal logged in user
     * @param id        user id
     * @return user information
     */
    @GetMapping("/{id}")
    public @ResponseBody
    UserView getUserInfo(Principal principal, @PathVariable Long id) {
        throwIfUserNotExists(id);
        if (hasPermission(principal, id)) {
            return new UserPrivateView(userRepository.getById(id));
        }
        return new UserPublicView(userRepository.getById(id));
    }

    /**
     * Deletes user and all personal chats with this user.
     *
     * @param id user id
     * @return user id
     */
    @Transactional
    @DeleteMapping("/{id}")
    public @ResponseBody
    Long deleteUser(Principal principal, @PathVariable Long id) {
        throwIfUserNotExists(id);
        if (!hasPermission(principal, id)) {
            throw new AccessDeniedException(
                    String.format("You don't have permission to delete user with id: %d", id));
        }
        userRepository.deleteMessagesById(id);
        userRepository.deleteChatMemberById(id);
        userRepository.deleteById(id);
        return id;
    }

    /**
     * Blocks user.
     *
     * @param principal logged in user
     * @param id        user id
     * @return user id
     */
    @PostMapping("/{id}/block")
    public @ResponseBody
    Long blockUser(Principal principal, @PathVariable Long id) {
        throwIfUserNotExists(id);
        if (!loggedInUser(principal).isAdmin()) {
            throw new AccessDeniedException("You don't have permission to block users");
        }
        userRepository.getById(id).setAccountType(AccountType.BLOCKED);
        return id;
    }

    /**
     * Returns a list of all chats available to user.
     * @param principal logged in user
     * @param id user id
     * @return list of chats
     */
    @GetMapping("/{id}/chats")
    public @ResponseBody
    List<ChatListView> getUserChats(Principal principal, @PathVariable Long id) {
        throwIfUserNotExists(id);
        if (!hasPermission(principal, id)) {
            throw new AccessDeniedException(
                    String.format("You don't have permission to access chat(s) of the user with id: %d", id));
        }

        List<ChatListView> chatList = new ArrayList<>();
        for (Chat chat : loggedInUser(principal).getChats()) {
            Message lastMessage = messageRepository.latestInChat(chat.getId());
            chatList.add(new ChatListView(chat, loggedInUser(principal),
                    lastMessage != null ? lastMessage.getText() : null));
        }
        return chatList;
    }

    @GetMapping("/whoami")
    public @ResponseBody Long whoAmI(Principal principal) {
        return loggedInUser(principal).getId();
    }

    /**
     * Returns the logged in user from principal as a User entity.
     *
     * @param principal logged in user
     * @return logged in user (as User)
     */
    private User loggedInUser(Principal principal) {
        return userRepository.getByUsername(principal.getName());
    }

    /**
     * Checks if the user with the given id exists.
     *
     * @param id user id
     * @throws UserNotFoundException user is not found with provided id
     */
    private void throwIfUserNotExists(Long id) throws UserNotFoundException {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException(id);
        }
    }

    /**
     * Checks if at least one user exists.
     *
     * @throws UserNotFoundException no user found
     */
    private void throwIfNoUserExists() throws UserNotFoundException {
        if (userRepository.findAll().isEmpty()) {
            throw new UserNotFoundException();
        }
    }

    /**
     * Checks if the logged in user has access to information available by id.
     *
     * @param principal logged in user
     * @param id        user id
     * @return true - has access, false - does not have
     */
    private boolean hasPermission(Principal principal, Long id) {
        return loggedInUser(principal).getId().equals(id) || loggedInUser(principal).isAdmin();
    }
}