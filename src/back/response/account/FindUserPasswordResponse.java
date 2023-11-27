package back.response.account;

import java.io.Serializable;

public record FindUserPasswordResponse(String password) implements Serializable {}
