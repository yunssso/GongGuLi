package back.request.board;

import java.io.Serializable;

public record ModifyMyPostRequest(int port, String title, String region, String category, String content) implements Serializable {}

