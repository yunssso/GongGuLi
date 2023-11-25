package back.request.board;

import java.io.Serializable;

public record BoardInfoRequest(String region, String category, String uuid) implements Serializable {}
