package back.response.chatroom;

import java.io.Serializable;

public record JoinMessageChatRoomResponse(String nickName, String message) implements Serializable {}
