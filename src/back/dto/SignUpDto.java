package back.dto;

import java.io.Serializable;

public record SignUpDto (String userId, String password, String passwordCheck, String name, String birth, String phoneNumber, String nickName, String region) implements Serializable {}