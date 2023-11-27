package back.response.chatroom;

import java.io.Serializable;

public record MessageChatRoomResponse(String nickName, String message) implements Serializable {}
