package back.dto;

import java.io.Serializable;

public record LoginDto(String userId, String password) implements Serializable {}