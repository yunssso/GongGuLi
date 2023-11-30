package back.dao.chatting;

import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IsMasterDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    public boolean isMaster(int port, String uuid) {
        try {
            conn = DBConnector.getConnection();
            String selectSQL = "SELECT EXISTS(SELECT 1 FROM chattingRoom WHERE port = ? AND masterUuid = ?) As cnt;";
            pt = conn.prepareStatement(selectSQL);
            pt.setInt(1, port);
            pt.setString(2, uuid);
            rs = pt.executeQuery();
            if (rs.next() && rs.getBoolean("cnt")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
