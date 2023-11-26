package back.request.board;

import java.io.Serializable;

public record BoardInfoMoreRequest(int selectRow, String uuid) implements Serializable {}
