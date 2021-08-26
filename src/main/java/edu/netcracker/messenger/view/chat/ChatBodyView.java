package edu.netcracker.messenger.view.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor
public class ChatBodyView {
    private List<Long> chatMembersId;

    private String chatName;

    private Long chatPictureId;

    public ChatBodyView(List<Long> chatMembersId) {
        this.chatMembersId = chatMembersId;
    }
}
