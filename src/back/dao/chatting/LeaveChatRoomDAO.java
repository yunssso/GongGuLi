package back.dao.chatting;

import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LeaveChatRoomDAO {
    Connection conn = null;
    PreparedStatement pt = null;

    public boolean leaveChatRoom(int port, String uuid) {
        try {
            conn = DBConnector.getConnection();
            String deleteSQL = "DELETE FROM chattingMember WHERE port = ? AND memberUuid = ?;";
            pt = conn.prepareStatement(deleteSQL);
            pt.setInt(1, port);
            pt.setString(2, uuid);
            pt.execute();

            String updateSQL = "UPDATE chattingRoom SET nowPeopleNum = nowPeopleNum - 1 WHERE port = ?;";
            pt = conn.prepareStatement(updateSQL);
            pt.setInt(1, port);
            pt.execute();

            updateSQL = "UPDATE board SET nowPeopleNum = nowPeopleNum - 1 WHERE port = ?;";
            pt = conn.prepareStatement(updateSQL);
            pt.setInt(1, port);
            pt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
