package back.request.board;

import java.io.Serializable;

public record PostBoardRequest(String title, String region, String category, String maxPeopleNum, String content, String uuid) implements Serializable {}
