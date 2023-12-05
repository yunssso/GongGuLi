package back.dao.chatting;

import back.request.chatroom.ChattingMessageRequest;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class InsertChattingMessageDAO {
    Connection conn = null;
    PreparedStatement pt = null;

    public void insertChattingMessage(ChattingMessageRequest chattingMessageRequest) {
        String insertSQL = "INSERT INTO chattingMessage (port, uuid, message, time) VALUES (?, ?, ?, CURRENT_TIMESTAMP);";

        try {
            conn = DBConnector.getConnection();
            pt = conn.prepareStatement(insertSQL);

            pt.setInt(1, chattingMessageRequest.port());
            pt.setString(2, chattingMessageRequest.uuid());
            pt.setString(3, chattingMessageRequest.message());

            pt.executeUpdate();

            pt.close();
            conn.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
