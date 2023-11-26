package back.request.chatroom;

import java.io.Serializable;

public record JoinMessageChatRoomRequest(String nickName, String uuid) implements Serializable {}
