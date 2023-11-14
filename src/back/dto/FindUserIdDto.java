package back.dto;

import java.io.Serializable;

public record FindUserIdDto(String name, String birth, String phoneNumber) implements Serializable {}