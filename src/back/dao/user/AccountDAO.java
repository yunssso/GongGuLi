package back.dao.user;

import back.response.mypage.UserInfoResponse;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

//    내 정보 불러오기
    public UserInfoResponse getMyInfo(String uuid) {
        try {
            conn = DBConnector.getConnection();
            String userInfoSQL = "SELECT nickName, name, userId, region, phoneNum, birth FROM user WHERE uuid = ?;";
            pt = conn.prepareStatement(userInfoSQL);
            pt.setString(1, uuid);
            rs = pt.executeQuery();

            if (rs.next()) {
                UserInfoResponse userInfoResponse = new UserInfoResponse(
                        rs.getString("nickName"),
                        rs.getString("name"),
                        rs.getString("userId"),
                        rs.getString("region"),
                        rs.getString("phoneNum"),
                        rs.getString("birth"));

                rs.close();
                pt.close();
                conn.close();

                return userInfoResponse;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

//    내 정보 수정
    public void modifyUserInfo() {

    }

//    회원 탈퇴
    public void withdrawlUserAccount() {

    }
}
