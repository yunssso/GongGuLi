package back.request.chatroom;

import java.io.Serializable;

public record JoinChatRoomRequest(int port, String uuid) implements Serializable {}