package back.request.chatroom;

import java.io.Serializable;

public record MessageChatRoomRequest(String message, String uuid) implements Serializable {}
