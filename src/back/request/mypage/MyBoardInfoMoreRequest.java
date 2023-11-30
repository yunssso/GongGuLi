package back.request.mypage;

import java.io.Serializable;

public record MyBoardInfoMoreRequest(int selectRow, String uuid) implements Serializable {}