package back.response;

import java.io.Serializable;

public record Board_Info_Response(String title, String region, String category, String peopleNum, String content, Boolean authority) implements Serializable {}
