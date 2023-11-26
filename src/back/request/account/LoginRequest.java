package back.request.account;

import java.io.Serializable;

public record LoginRequest(String userId, String password) implements Serializable {}