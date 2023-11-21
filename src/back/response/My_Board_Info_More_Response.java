package back.response;

import java.io.Serializable;

public record My_Board_Info_More_Response(int boardId, String title, String region, String category, String peopleNum, String content, int view) implements Serializable {}