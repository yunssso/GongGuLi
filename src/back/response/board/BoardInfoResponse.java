package back.response.board;

import java.io.Serializable;
import java.sql.Timestamp;

public record BoardInfoResponse(String region, String category, String title, String writer, String peopleNum, Timestamp postingTime) implements Serializable {}
