package back.request.board;

import java.io.Serializable;

public record Post_Board_Request(String title, String region, String category, String peopleNum, String content, String uuid) implements Serializable {}
