package back.request.board;

import java.io.Serializable;

public record SearchBoardInfoRequest(String searchFilter, String searchText, String uuid, String region, String category) implements Serializable {}
