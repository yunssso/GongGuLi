package back.dao.user;

import back.request.account.FindUserIdRequest;
import back.request.account.FindUserPasswordRequest;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FindUserDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    public String findID(FindUserIdRequest findUserIdInfo) {
    conn = DBConnector.getConnection();
    String selectsql = "select userId from user where name = ? and birth = ? and phoneNum= ?;";
    try {
        pt = conn.prepareStatement(selectsql);
        pt.setString(1, findUserIdInfo.name());
        pt.setString(2, findUserIdInfo.birth());
        pt.setString(3, findUserIdInfo.phoneNumber());
        rs = pt.executeQuery();
        if (rs.next()) {
            return rs.getString(1);
        }
    } catch (Exception exception) {
        exception.printStackTrace();
    }

    return "";
}

    public String findPassword(FindUserPasswordRequest findUserPasswordRequest) {
        conn = DBConnector.getConnection();
        String selectsql = "select password from user where name = ? and userID = ? and birth = ? and phoneNum= ?;";
        try {
            pt = conn.prepareStatement(selectsql);
            pt.setString(1, findUserPasswordRequest.name());
            pt.setString(2, findUserPasswordRequest.userId());
            pt.setString(3, findUserPasswordRequest.birth());
            pt.setString(4, findUserPasswordRequest.phoneNumber());
            rs = pt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return "";
    }
}
