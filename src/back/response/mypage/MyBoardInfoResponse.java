package back.response.mypage;

import java.io.Serializable;

public record MyBoardInfoResponse(String region, String category, String title, String peopleNum)implements Serializable {}
