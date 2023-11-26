package back.dao;

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
}
