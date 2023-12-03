package back.response.mypage;

import java.io.Serializable;
import java.sql.Timestamp;

public record MyHistoryInfoResponse(String region, String category, String title, String writer, String peopleNum, Timestamp postingTime) implements Serializable {
}
