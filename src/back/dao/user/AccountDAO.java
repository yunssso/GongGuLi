package back.dao.user;

import back.dao.GetInfoDAO;
import back.request.account.ModifyUserInfoRequest;
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
    public int modifyUserInfo(ModifyUserInfoRequest modifyUserInfoRequest) {
        GetInfoDAO getInfoDAO = new GetInfoDAO();
        String nickName = getInfoDAO.getNickNameMethod(modifyUserInfoRequest.uuid());
        try {
            conn = DBConnector.getConnection();
            String updateSQL = "UPDATE user SET nickName = ?, password = ?, region = ? where uuid = ?";
            pt = conn.prepareStatement(updateSQL);
            pt.setString(1, modifyUserInfoRequest.nickName());
            pt.setString(2, modifyUserInfoRequest.password());
            pt.setString(3, modifyUserInfoRequest.region());
            pt.setString(4, modifyUserInfoRequest.uuid());
            if (!pt.execute()) {
                return 1;   //  닉네임 수정 성공
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("정보 수정 DB 오류");
        }
        return 0;   //  실패
    }

    //    회원 탈퇴
    public boolean deleteUser(String uuid, String password) {
        if (passWordCheck(uuid, password)) {
            try {
                conn = DBConnector.getConnection();
                String deleteSQL = "DELETE FROM user WHERE uuid = ?";
                pt = conn.prepareStatement(deleteSQL);
                pt.setString(1, uuid);
                if (!pt.execute()) {
                    System.out.println("DAO에서 회원 탈퇴 성공");
                    pt.close();
                    conn.close();
                    return true;
                }
                pt.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean passWordCheck(String uuid, String password) {
        try {
            conn = DBConnector.getConnection();
            String checkSQL = "SELECT password FROM user where uuid = ?;";
            pt = conn.prepareStatement(checkSQL);
            pt.setString(1, uuid);
            rs = pt.executeQuery();
            if (rs.next() && password.equals(rs.getString("password"))) {
                rs.close();
                pt.close();
                conn.close();
                return true;
            }
            rs.close();
            pt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
