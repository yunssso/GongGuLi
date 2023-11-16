package back.response;

import java.io.Serializable;

public record FindUserPasswordResponse(String password) implements Serializable {}
