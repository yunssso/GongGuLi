package back.dao;

import back.response.mypage.UserInfoResponse;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetInfoDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    public String getnickNameMethod(String uuid) {
        String nickNameSQL = "SELECT nickName FROM user WHERE uuid = ?;";

        try {
            conn = DBConnector.getConnection();
            pt = conn.prepareStatement(nickNameSQL);
            pt.setString(1, uuid);
            rs = pt.executeQuery();

            if (rs.next()) {
                String nickName = rs.getString("nickName");

                rs.close();
                pt.close();
                conn.close();

                return nickName;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public int getchatPortMethod(int selectRow) {
        selectRow++;

        String selectSQL = "SELECT chatPort FROM boardView WHERE num = ?;";

        try {
            conn = DBConnector.getConnection();
            pt = conn.prepareStatement(selectSQL);
            pt.setInt(1, selectRow);
            rs = pt.executeQuery();

            if (rs.next()) {
                int chatPort = rs.getInt("chatPort");

                rs.close();
                pt.close();
                conn.close();

                return chatPort;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1; // chatPort를 가져오지 못했을 때의 반환값, 적절한 값으로 변경 필요
    }

    public UserInfoResponse getUserInfo(String uuid) {
        String userInfoSQL = "SELECT nickName, name, userId, region, phoneNum, birth FROM user WHERE uuid = ?;";

        try {
            conn = DBConnector.getConnection();
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
}