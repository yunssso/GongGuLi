package back.response.board;

import java.io.Serializable;

public record Board_Info_More_Response(int port, String title, String region, String category, String nickName, String peopleNum, String content, int view, Boolean authority) implements Serializable {}
