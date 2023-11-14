package back.Server.Dto;

import java.io.Serializable;

public record SignUpDto (String userId, char[] password, char[] passwordCheck, String name, String birth, String phoneNumber, String nickName, String region) implements Serializable {}