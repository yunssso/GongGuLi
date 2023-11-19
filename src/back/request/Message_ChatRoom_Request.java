package back.request;

import java.io.Serializable;

public record Message_ChatRoom_Request(String nickName, String message, String uuid) implements Serializable {}
