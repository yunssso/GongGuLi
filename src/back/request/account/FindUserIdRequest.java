package back.request.account;

import java.io.Serializable;

public record FindUserIdRequest(String name, String birth, String phoneNumber) implements Serializable {}