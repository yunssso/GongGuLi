package back.request.account;

import java.io.Serializable;

public record NickNameCheckRequest(String inpNickName) implements Serializable {
}
