package edu.netcracker.messenger.controller.api.message;

import edu.netcracker.messenger.model.message.Message;
import edu.netcracker.messenger.model.message.MessageRepository;
import edu.netcracker.messenger.model.message.exception.MessageNotFoundException;
import edu.netcracker.messenger.model.user.User;
import edu.netcracker.messenger.model.user.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

/**
 * Controller for handling requests related to messages.
 */
@Controller
@RequestMapping("api/message")
public class MessageApiController {

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    public MessageApiController(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    /**
     * Marks all messages as read.
     * @param principal logged in user
     * @param messageId message id
     * @return message id
     * @throws AccessDeniedException user don't have permission to access message
     */
    @PostMapping("{messageId}/read")
    public @ResponseBody
    Long markMessageAsRead(Principal principal, @PathVariable Long messageId) throws AccessDeniedException {
        throwIfMessageHasErrors(principal, messageId);
        for (Message message : messageRepository.findPreviousMessages(messageId)) {
            if (message.getReadDate() == null) {
                message.markAsRead();
                messageRepository.save(message);
            }
        }
        return messageId;
    }

    /**
     * Deletes message and/or associated attachment.
     * @param principal logged in user
     * @param messageId message id
     * @return deleted message id
     * @throws AccessDeniedException user don't have permission to access message
     */
    @DeleteMapping("{messageId}")
    public @ResponseBody Long deleteMessage(Principal principal, @PathVariable Long messageId)
            throws AccessDeniedException {
        throwIfMessageHasErrors(principal, messageId);
        messageRepository.deleteById(messageId);
        return messageId;
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
     * Checks if the message request is valid. It is checked whether the message exists,
     * as well as whether the user has access to it.
     * @param principal logged in user
     * @param messageId message id
     * @throws MessageNotFoundException no message found with provided id
     * @throws AccessDeniedException user don't have permission to access message
     */
    private void throwIfMessageHasErrors(Principal principal, Long messageId) throws MessageNotFoundException,
            AccessDeniedException {
        if (messageRepository.findById(messageId).isEmpty()) {
            throw new MessageNotFoundException(messageId);
        }
        Message message = messageRepository.getById(messageId);
        if (!loggedInUser(principal).getId().equals(message.getSenderId()) || !loggedInUser(principal).isAdmin()) {
            throw new AccessDeniedException("You don't have permissions to this message");
        }
    }
}
