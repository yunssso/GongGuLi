package back.dto;

import java.io.Serializable;

public record Post_BoardDto(String title, String region, String category, String peopleNum, String content, String uuid) implements Serializable {}
