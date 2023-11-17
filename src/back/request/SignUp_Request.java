package back.request;

import java.io.Serializable;

public record SignUp_Request(String userId, String password, String passwordCheck, String name, String birth, String phoneNumber, String nickName, String region) implements Serializable {}