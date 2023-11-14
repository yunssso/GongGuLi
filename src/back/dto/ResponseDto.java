package back.dto;

import java.io.Serializable;

public record ResponseDto(int responseCode, String message) implements Serializable {}