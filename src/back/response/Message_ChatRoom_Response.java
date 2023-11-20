package back.response;

import java.io.Serializable;

public record Message_ChatRoom_Response(String nickName, String message) implements Serializable {}
