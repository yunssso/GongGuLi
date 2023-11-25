package back.response.chatroom;

import java.io.Serializable;

public record GetChattingRoomResponse(String region, String category, String title, String writerUuid, String lastUpdatedTime) implements Serializable {
}
