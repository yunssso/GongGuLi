package back.response.board;

import java.io.Serializable;

public record Board_Info_Response(String region, String category, String title, String writer, String peopleNum) implements Serializable {}
