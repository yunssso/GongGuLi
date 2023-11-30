package back.dao.user;

import back.request.account.FindUserIdRequest;
import back.request.account.FindUserPasswordRequest;
import back.response.account.FindUserIdResponse;
import back.response.account.FindUserPasswordResponse;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FindUserDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    public FindUserIdResponse findID(FindUserIdRequest findUserIdInfo) {
    try {
        conn = DBConnector.getConnection();
        String selectSQL = "select userId from user where name = ? and birth = ? and phoneNum= ?;";
        pt = conn.prepareStatement(selectSQL);
        pt.setString(1, findUserIdInfo.name());
        pt.setString(2, findUserIdInfo.birth());
        pt.setString(3, findUserIdInfo.phoneNumber());
        rs = pt.executeQuery();
        if (rs.next()) {
            return new FindUserIdResponse(rs.getString(1));
        }
    } catch (Exception exception) {
        exception.printStackTrace();
    }

    return null;
}

    public FindUserPasswordResponse findPassword(FindUserPasswordRequest findUserPasswordRequest) {
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
                return new FindUserPasswordResponse(rs.getString(1));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
