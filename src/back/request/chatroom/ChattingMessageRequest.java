package back.request.chatroom;

import java.io.Serializable;

public record ChattingMessageRequest(String message, String uuid, int port) implements Serializable {}
