package edu.netcracker.messenger.model.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "messages")
@JsonIgnoreProperties({"id", "creation_date", "read_date"})
public class Message {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messages_id_seq")
    @SequenceGenerator(name = "messages_id_seq", sequenceName = "messages_id_seq", allocationSize = 1)
    @Getter private Long id;

    @Column(name = "chat_id")
    @Getter private Long chatId;

    @Column(name = "user_id")
    @Getter private Long senderId;

    @Column(name = "text")
    @Getter private String text;

    @Column(name = "attachment_id")
    @Getter private Long attachmentId;

    @Column(name = "creation_date")
    @Getter private final LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "read_date")
    @Getter private LocalDateTime readDate;

    public Message(Long chatId, Long senderId, String text) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.text = text;
    }

    public void markAsRead() {
        readDate = LocalDateTime.now();
    }
}
