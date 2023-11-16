package back.dto;

import java.io.Serializable;

public record FindUserPasswordDto(String name, String userId, String birth, String phoneNumber) implements Serializable  {}
