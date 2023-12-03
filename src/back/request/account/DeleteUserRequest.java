package back.request.account;

import java.io.Serializable;

public record DeleteUserRequest(String password, String uuid) implements Serializable {}
