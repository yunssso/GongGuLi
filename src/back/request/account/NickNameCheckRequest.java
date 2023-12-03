package back.request.account;

import java.io.Serializable;

public record NickNameCheckRequest(String uuid, String inpNickName) implements Serializable {
}
