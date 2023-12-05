package back.response.chatroom;

import java.io.Serializable;

public record ChattingMessageResponse(String message, String nickName) implements Serializable {}
