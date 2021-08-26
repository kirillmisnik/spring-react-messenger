package edu.netcracker.messenger.model.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.netcracker.messenger.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "chats")
@JsonIgnoreProperties({"id", "members", "chat_type", "creation_date"})
public class Chat {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chats_id_seq")
    @SequenceGenerator(name = "chats_id_seq", sequenceName = "chats_id_seq", allocationSize = 1)
    @Getter private Long id;

    @Column(name = "chat_type")
    @Enumerated(EnumType.STRING)
    @Getter private ChatType chatType = ChatType.PERSONAL;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "chat_members",
            joinColumns = { @JoinColumn(name = "chat_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    @Getter private List<User> members;

    @Column(name = "creation_date")
    @Getter private final LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "chat_name")
    @Getter @Setter private String chatName;

    @Column(name = "chat_picture_id")
    @Getter @Setter private Long chatPictureId;

    public Chat(List<User> members, String chatName, Long chatPictureId) {
        this.members = members;
        if (members.size() > 2) {
            this.chatName = chatName;
            this.chatPictureId = chatPictureId;
            chatType = ChatType.GROUP;
        }
    }

    public boolean isPersonal() {
        return getChatType().equals(ChatType.PERSONAL);
    }
}
