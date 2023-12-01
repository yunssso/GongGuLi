package back.dao.chatting;

import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MakeChattingRoomDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    public int makeChattingRoom() {
        try {
            conn = DBConnector.getConnection();
            String insertSQL = "";
            pt = conn.prepareStatement(insertSQL);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
