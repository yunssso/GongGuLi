package back.request.board;

import java.io.Serializable;

public record SearchBoardInfoRequest(String searchFilter, String searchText, String uuid) implements Serializable {}
