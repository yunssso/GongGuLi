package back.response.mypage;

import java.io.Serializable;

public record UserInfoResponse(String nickName, String name, String userId, String region, String phoneNum, String birth) implements Serializable {}
