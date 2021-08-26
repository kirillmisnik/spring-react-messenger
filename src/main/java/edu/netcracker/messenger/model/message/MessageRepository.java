package edu.netcracker.messenger.model.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * A set of database queries related to messages.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Finds all messages in the chat.
     * @param chatId chat id
     * @return list of messages
     */
    @Query(value = "SELECT * FROM messages WHERE chat_id = ?1", nativeQuery = true)
    List<Message> findMessages(Long chatId);

    /**
     * Finds the last message in the chat.
     * @param chatId chat id
     * @return message
     */
    @Query(value = "SELECT * FROM messages WHERE chat_id = ?1 ORDER BY creation_date DESC LIMIT 1", nativeQuery = true)
    Message latestInChat(Long chatId);

    /**
     * Returns all messages by page.
     * @param chatId chat id
     * @param pageSize page size
     * @param pageNum page number
     * @return list of messages
     */
    @Query(value = "SELECT * FROM messages WHERE chat_id = ?1 ORDER BY creation_date LIMIT ?2 OFFSET (?3 - 1) * ?2", nativeQuery = true)
    List<Message> findMessagesByPage(Long chatId, Integer pageSize, Integer pageNum);

    /**
     * Finds all previous messages.
     * @param messageId message id
     * @return list of messages
     */
    @Query(value = "SELECT * FROM messages WHERE creation_date <= (SELECT creation_date FROM messages WHERE id = ?1) AND chat_id = (SELECT chat_id FROM messages WHERE id = ?1) ORDER BY creation_date", nativeQuery = true)
    List<Message> findPreviousMessages(Long messageId);
}

