package back.request.account;

import java.io.Serializable;

public record ModifyUserInfoRequest(String nickName, String password, String passwordCheck, String region, String uuid) implements Serializable {
}
