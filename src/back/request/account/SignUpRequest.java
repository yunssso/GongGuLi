package back.request.account;

import java.io.Serializable;

public record SignUpRequest(String userId, String password, String passwordCheck, String name, String birth, String phoneNumber, String nickName, String region) implements Serializable {}