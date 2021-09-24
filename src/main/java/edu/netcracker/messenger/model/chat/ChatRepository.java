package edu.netcracker.messenger.model.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(value = "SELECT * FROM chats WHERE id IN (SELECT chat_id FROM chat_members WHERE user_id = ?1)", nativeQuery = true)
    List<Chat> getChatsByUserId(Long id);

    @Modifying
    @Query(value = "DELETE FROM messages WHERE chat_id = ?1", nativeQuery = true)
    void deleteMessagesById(Long id);

    @Modifying
    @Query(value = "DELETE FROM chat_members WHERE chat_id = ?1", nativeQuery = true)
    void deleteChatById(Long id);

    @Override
    @Modifying
    @Query(value = "DELETE FROM chats WHERE id = ?1", nativeQuery = true)
    void deleteById(Long id);
}
