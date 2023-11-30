package back.request.chatroom;

import java.io.Serializable;

public record JoinMessageChatRoomRequest(String uuid) implements Serializable {}
