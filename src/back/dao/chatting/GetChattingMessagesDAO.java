package back.dao.chatting;

import back.request.chatroom.ChattingMessageRequest;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GetChattingMessagesDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    public List<ChattingMessageRequest> getChattingMessages(int port) {
        List<ChattingMessageRequest> messages = new ArrayList<>();
        String selectSQL = "SELECT uuid, message, time FROM chattingMessage WHERE port = ? ORDER BY time;";

        try {
            conn = DBConnector.getConnection();
            pt = conn.prepareStatement(selectSQL);
            pt.setInt(1, port);
            rs = pt.executeQuery();

            while (rs.next()) {
                String uuid = rs.getString("uuid");
                String message = rs.getString("message");
                // 여기서 필요하다면 시간(time)도 가져와서 활용할 수 있습니다.

                ChattingMessageRequest chattingMessageRequest = new ChattingMessageRequest(message, uuid, port);
                messages.add(chattingMessageRequest);
            }

            rs.close();
            pt.close();
            conn.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return messages;
    }
}
