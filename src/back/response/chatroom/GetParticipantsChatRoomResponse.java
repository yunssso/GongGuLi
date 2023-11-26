package back.response.chatroom;

import java.io.Serializable;
import java.util.ArrayList;

public record GetParticipantsChatRoomResponse(ArrayList<String> list) implements Serializable {}
