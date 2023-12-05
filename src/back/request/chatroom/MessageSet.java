package back.request.chatroom;

import java.io.Serializable;

public record MessageSet(String message, String uuid, int port) implements Serializable {}
