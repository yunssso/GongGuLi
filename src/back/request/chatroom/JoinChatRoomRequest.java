package back.request.chatroom;

import java.io.Serializable;

public record JoinChatRoomRequest(String uuid) implements Serializable {}