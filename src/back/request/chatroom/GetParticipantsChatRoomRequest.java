package back.request.chatroom;

import java.io.Serializable;

public record GetParticipantsChatRoomRequest (int port) implements Serializable {}
