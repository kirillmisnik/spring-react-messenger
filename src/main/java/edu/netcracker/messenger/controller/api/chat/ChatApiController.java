package edu.netcracker.messenger.controller.api.chat;

import edu.netcracker.messenger.model.chat.Chat;
import edu.netcracker.messenger.model.chat.ChatRepository;
import edu.netcracker.messenger.model.chat.exception.ChatAlreadyExistsException;
import edu.netcracker.messenger.model.chat.exception.ChatNotFoundException;
import edu.netcracker.messenger.model.message.Message;
import edu.netcracker.messenger.model.message.MessageRepository;
import edu.netcracker.messenger.model.user.User;
import edu.netcracker.messenger.model.user.UserRepository;
import edu.netcracker.messenger.model.user.exception.UserNotFoundException;
import edu.netcracker.messenger.view.chat.ChatBodyView;
import edu.netcracker.messenger.view.chat.ChatView;
import edu.netcracker.messenger.view.message.MessageBodyView;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller for handling requests related to chats.
 */
@Controller
@RequestMapping("api/chat")
public class ChatApiController {

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    public ChatApiController(ChatRepository chatRepository,
                          MessageRepository messageRepository,
                          UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a chat. Depending on the number of users, the chat can be personal or group.
     * @param principal logged in user
     * @param chatBody chat parameters
     * @return information about the created chat
     */
    @PostMapping("/create")
    public @ResponseBody
    ChatView createChat(Principal principal, @RequestBody ChatBodyView chatBody) {
        List<User> members = findUsersByUsername(principal, chatBody);
        Chat chat = new Chat(members, chatBody.getChatName(), chatBody.getChatPictureId());
        if (chat.isPersonal()) {
            checkIfPersonalChatExists(principal, members);
        }
        chatRepository.save(chat);
        return new ChatView(chat, loggedInUser(principal));
    }

    /**
     * Returns chat information. The information depends on the user who makes the request.
     * @param principal logged in user
     * @param chatId chat id
     * @return chat information
     */
    @GetMapping("/{chatId}")
    public @ResponseBody
    ChatView getChatInfo(Principal principal, @PathVariable Long chatId) {
        throwIfChatHasErrors(principal, chatId);
        return new ChatView(chatRepository.getById(chatId),
                userRepository.getByUsername(principal.getName()));
    }

    /**
     * Returns all chat messages.
     * @param principal logged in user
     * @param chatId chat id
     * @return chat messages
     */
    @GetMapping("/{chatId}/messages/all")
    public @ResponseBody
    List<Message> getChatMessages(Principal principal, @PathVariable Long chatId) {
        throwIfChatHasErrors(principal, chatId);
        return messageRepository.findMessages(chatId);
    }

    /**
     * Returns chat messages by page. if no page-size or page-number
     * is specified, the defaults values are used (page-size = 10, page-num = 1).
     * @param principal logged in user
     * @param chatId chat id
     * @param pageSize page size
     * @param pageNum page number
     * @return chat messages by page
     */
    @GetMapping("/{chatId}/messages")
    public @ResponseBody List<Message> getChatMessagesByPage(Principal principal, @PathVariable Long chatId,
                                                             @RequestParam(name = "page_size", required = false) Integer pageSize,
                                                             @RequestParam(name = "page_num", required = false) Integer pageNum) {
        throwIfChatHasErrors(principal, chatId);
        return messageRepository.findMessagesByPage(chatId, pageSize == null ? 10 : pageSize, pageNum == null ? 1 : pageNum);
    }

    /**
     * Returns the last message from the chat.
     * @param principal logged in user
     * @param chatId chat id
     * @return last chat message
     */
    @GetMapping("/{chatId}/latest")
    public @ResponseBody Message getLastMessage(Principal principal, @PathVariable Long chatId) {
        throwIfChatHasErrors(principal, chatId);
        return messageRepository.latestInChat(chatId);
    }

    /**
     * Deletes chat and all associated messages and attachments.
     * @param principal logged in user
     * @param chatId chat id
     * @return deleted chat id
     */
    @DeleteMapping("/{chatId}")
    public @ResponseBody Long deleteChat(Principal principal, @PathVariable Long chatId) {
        throwIfChatHasErrors(principal, chatId);
        messageRepository.deleteAll(messageRepository.findMessages(chatId));
        chatRepository.deleteById(chatId);
        return chatId;
    }

    /**
     * Sends a message to the chat. The message must contain text and/or attachment.
     * @param principal logged in user
     * @param chatId chat id
     * @param messageBody message parameters
     * @return chat id to which the message was sent
     */
    @PostMapping("/{chatId}")
    public @ResponseBody
    Long sendMessage(Principal principal, @PathVariable Long chatId, @RequestBody MessageBodyView messageBody) {
        throwIfChatHasErrors(principal, chatId);
        Message message = new Message(chatId, userRepository.getByUsername(principal.getName()).getId(),
                messageBody.getText());
        messageRepository.save(message);
        return message.getId();
    }

    /**
     * Returns the logged in user from principal as a User entity.
     * @param principal logged in user
     * @return logged in user (as User)
     */
    private User loggedInUser(Principal principal) {
        return userRepository.getByUsername(principal.getName());
    }

    /**
     * Returns a list of users from the list of id's.
     * @param principal logged in user
     * @param chatBody chat parameters
     * @return list of users
     * @throws UserNotFoundException no user found with provided id
     */
    private List<User> findUsersByUsername(Principal principal, ChatBodyView chatBody) throws UserNotFoundException {
        List<User> users = new ArrayList<>(Collections.singletonList(loggedInUser(principal)));
        List<Long> errors = new ArrayList<>();
        for (String participantUsername : chatBody.getChatMembersUsername()) {
            if (userRepository.getByUsername(participantUsername) == null) {
                errors.add(userRepository.getByUsername(participantUsername).getId());
            } else {
                users.add(userRepository.getByUsername(participantUsername));
            }
        }
        if (!errors.isEmpty()) {
            throw new UserNotFoundException(errors);
        }
        return users;
    }

    private void checkIfPersonalChatExists(Principal principal, List<User> members) throws ChatAlreadyExistsException {
        for (Chat c : loggedInUser(principal).getChats()) {
            if (c.getMembers().containsAll(members)) {
                throw new ChatAlreadyExistsException();
            }
        }
    }

    /**
     * Checks if the chat request is valid. It is checked whether the chat exists,
     * as well as whether the user has access to it.
     * @param principal logged in user
     * @param chatId chat id
     * @throws ChatNotFoundException no chat found with provided id
     * @throws AccessDeniedException user don't have permission to access chat
     */
    private void throwIfChatHasErrors(Principal principal, Long chatId) throws ChatNotFoundException,
            AccessDeniedException {
        if (chatRepository.findById(chatId).isEmpty()) {
            throw new ChatNotFoundException(chatId);
        }
        if (!chatRepository.getById(chatId).getMembers().contains(loggedInUser(principal))) {
            throw new AccessDeniedException("You don't have permission to chat with id: " + chatId);
        }
    }
}
