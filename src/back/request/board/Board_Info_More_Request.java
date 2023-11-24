package back.request.board;

import java.io.Serializable;

public record Board_Info_More_Request(int selectRow, String uuid) implements Serializable {}
