package edu.netcracker.messenger.view.chat;

import edu.netcracker.messenger.model.chat.Chat;
import edu.netcracker.messenger.model.chat.ChatType;
import edu.netcracker.messenger.model.user.User;
import edu.netcracker.messenger.view.user.UserPublicView;
import edu.netcracker.messenger.view.user.UserView;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChatView {
    private final Long id;

    private final ChatType type;

    private String name;

    private final List<UserView> members = new ArrayList<>();

    public ChatView(Chat chat, User user) {
        id = chat.getId();
        type = chat.getChatType();
        name = chat.getChatName();
        for (User member : chat.getMembers()) {
            members.add(new UserPublicView(member));
            if (type.equals(ChatType.PERSONAL) && member != user) {
                name = member.getFirstName() + ' ' + member.getLastName();
            }
        }
    }
}
