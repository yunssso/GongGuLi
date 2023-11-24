package back.request.board;

import java.io.Serializable;

public record Board_Info_Request(String region, String category, String uuid) implements Serializable {}
