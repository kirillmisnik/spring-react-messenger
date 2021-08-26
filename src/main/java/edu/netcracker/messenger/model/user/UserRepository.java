package edu.netcracker.messenger.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>  {

    @Query(value = "SELECT * FROM users WHERE username = ?1", nativeQuery = true)
    User getByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE phone_number = ?1", nativeQuery = true)
    User getByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
    User getByEmail(String email);

    @Modifying
    @Query(value = "DELETE FROM chat_members WHERE user_id = ?1", nativeQuery = true)
    void deleteChatMemberById(Long id);

    @Modifying
    @Query(value = "DELETE FROM messages WHERE user_id = ?1", nativeQuery = true)
    void deleteMessagesById(Long id);
}
