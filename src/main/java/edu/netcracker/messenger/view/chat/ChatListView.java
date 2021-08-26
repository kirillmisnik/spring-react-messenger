package edu.netcracker.messenger.view.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
public class ChatListView {
    private Long chatId;
    private String chatName;
    private Long chatPictureId;
    private String lastMessage;
}
