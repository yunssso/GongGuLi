package back.request.account;

import java.io.Serializable;

public record Find_UserPassword_Request(String name, String userId, String birth, String phoneNumber) implements Serializable  {}
