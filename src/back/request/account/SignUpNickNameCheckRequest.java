package back.request.account;

import java.io.Serializable;

public record SignUpNickNameCheckRequest(String inpNickName)implements Serializable {
}
