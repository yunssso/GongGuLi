package back.dao.board;

import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteBoardDAO {
    Connection conn = null;
    PreparedStatement pt = null;

    public boolean deleteBoardMethod(int port) {
        try {
            conn = DBConnector.getConnection();
            String deleteSQL = "DELETE FROM board WHERE port = ?;";
            pt = conn.prepareStatement(deleteSQL);
            pt.setInt(1, port);
            pt.execute();

            deleteSQL = "DELETE FROM chattingRoom WHERE port = ?;";
            pt = conn.prepareStatement(deleteSQL);
            pt.setInt(1, port);
            pt.execute();

            deleteSQL = "DELETE FROM chattingMember WHERE port = ?;";
            pt = conn.prepareStatement(deleteSQL);
            pt.setInt(1, port);
            pt.execute();

            deleteSQL = "DELETE FROM chattingMessage WHERE port = ?;";
            pt = conn.prepareStatement(deleteSQL);
            pt.setInt(1, port);
            pt.execute();

            pt.close();
            conn.close();
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }
}
