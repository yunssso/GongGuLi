package back.request.chatroom;

import java.io.Serializable;

public record GetChattingRoomRequest(String uuid) implements Serializable {
}
