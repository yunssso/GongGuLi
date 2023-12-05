package back.request.chatroom;

import java.io.Serializable;

public record LeaveChatRoomRequest(int port, String uuid) implements Serializable {}
