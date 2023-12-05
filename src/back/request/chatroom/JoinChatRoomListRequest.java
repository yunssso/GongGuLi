package back.request.chatroom;

import java.io.Serializable;

public record JoinChatRoomListRequest(int selectRow, String uuid) implements Serializable {}
