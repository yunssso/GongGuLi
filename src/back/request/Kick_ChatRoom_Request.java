package back.request;

import java.io.Serializable;

public record Kick_ChatRoom_Request(String target_nickName, String uuid) implements Serializable {}
