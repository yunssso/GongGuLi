package back.response.chatroom;

import java.io.Serializable;

public record Join_ChatRoom_Response(String nickName, int chatPort) implements Serializable {}
