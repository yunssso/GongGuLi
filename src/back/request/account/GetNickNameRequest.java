package back.request.account;

import java.io.Serializable;

public record GetNickNameRequest(String uuid) implements Serializable {
}
