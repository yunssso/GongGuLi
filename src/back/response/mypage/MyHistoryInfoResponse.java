package back.response.mypage;

import java.io.Serializable;

public record MyHistoryInfoResponse(String region, String category, String title, String writer, String peopleNum) implements Serializable {
}
