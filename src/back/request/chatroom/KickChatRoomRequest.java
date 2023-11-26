package back.request.chatroom;

import java.io.Serializable;

public record KickChatRoomRequest(String target_nickName, String uuid) implements Serializable {}
