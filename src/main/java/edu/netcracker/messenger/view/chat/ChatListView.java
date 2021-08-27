package edu.netcracker.messenger.view.chat;

import edu.netcracker.messenger.model.chat.Chat;
import edu.netcracker.messenger.model.chat.ChatType;
import edu.netcracker.messenger.model.user.User;
import lombok.Getter;

@Getter
public class ChatListView {
    private final Long id;
    private String name;
    private final Long pictureId;
    private final String lastMessage;

    public ChatListView(Chat chat, User user, String lastMessage) {
        id = chat.getId();
        name = chat.getChatName();
        pictureId = chat.getChatPictureId();
        for (User member : chat.getMembers()) {
            if (chat.getChatType().equals(ChatType.PERSONAL) && member != user) {
                name = member.getFirstName() + ' ' + member.getLastName();
            }
        }
        this.lastMessage = lastMessage;
    }
}
