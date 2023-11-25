package back.request.chatroom;

import java.io.Serializable;

public record JoinChatRoomRequest(int selectRow, String uuid) implements Serializable {}