package back.response.board;

import java.io.Serializable;

public record BoardInfoResponse(String region, String category, String title, String writer, String peopleNum) implements Serializable {}
