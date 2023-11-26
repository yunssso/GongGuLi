package back.request.board;

import java.io.Serializable;

public record PostBoardRequest(String title, String region, String category, String peopleNum, String content, String uuid) implements Serializable {}
