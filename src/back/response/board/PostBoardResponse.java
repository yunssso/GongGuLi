package back.response.board;

import java.io.Serializable;

public record PostBoardResponse(String nickName, int port) implements Serializable {}
