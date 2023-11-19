package back.request;

import java.io.Serializable;

public record Join_ChatRoom_Request(int selectRow, String uuid) implements Serializable {}