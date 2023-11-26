package back.response.mypage;

import java.io.Serializable;

public record MyBoardInfoMoreResponse(int port, String title, String region, String category, String peopleNum, String content, int view) implements Serializable {}