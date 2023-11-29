package back.response.chatroom;

import java.io.Serializable;

public record JoinChatRoomResponse(String nickName) implements Serializable {}