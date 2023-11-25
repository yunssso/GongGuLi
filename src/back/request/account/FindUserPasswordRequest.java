package back.request.account;

import java.io.Serializable;

public record FindUserPasswordRequest(String name, String userId, String birth, String phoneNumber) implements Serializable  {}
