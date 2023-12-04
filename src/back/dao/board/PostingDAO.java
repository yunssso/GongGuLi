package back.dao.board;

import back.dao.chatting.JoinChattingRoomDAO;
import back.request.board.PostBoardRequest;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostingDAO {
    Connection conn = null;
    PreparedStatement pt = null;

    public boolean posting(PostBoardRequest postBoardRequest, int port) {
        boolean isPosted = false;
        String insertSQL = "INSERT INTO board(title, region, category, maxPeopleNum, content, uuid, view, nowPeopleNum, port) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = DBConnector.getConnection();
            pt = conn.prepareStatement(insertSQL);
            pt.setString(1, postBoardRequest.title());
            pt.setString(2, postBoardRequest.region());
            pt.setString(3, postBoardRequest.category());
            pt.setString(4, postBoardRequest.maxPeopleNum());
            pt.setString(5, postBoardRequest.content());
            pt.setString(6, postBoardRequest.uuid());
            pt.setInt(7, 0);
            pt.setInt(8, 0);
            pt.setInt(9, port);

            if (!pt.execute()) {
                isPosted = true;
                System.out.println("게시 성공.");
            }

            JoinChattingRoomDAO joinChattingRoomDAO = new JoinChattingRoomDAO();
            joinChattingRoomDAO.joinChattingRoom(port, postBoardRequest.uuid());

            pt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("게시 실패.");
            e.printStackTrace();
        }

        return isPosted;
    }
}
