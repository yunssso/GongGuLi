package back.dao;

import back.request.board.Post_Board_Request;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostingDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    public void posting(Post_Board_Request Post_BoardInfo, int port) {
        conn = DBConnector.getConnection();
        String insertSQL = "INSERT INTO board(title, region, category, peopleNum, content, uuid, view, nowPeopleNum, port) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pt = conn.prepareStatement(insertSQL);
            pt.setString(1, Post_BoardInfo.title());
            pt.setString(2, Post_BoardInfo.region());
            pt.setString(3, Post_BoardInfo.category());
            pt.setString(4, Post_BoardInfo.peopleNum());
            pt.setString(5, Post_BoardInfo.content());
            pt.setString(6, Post_BoardInfo.uuid());
            pt.setInt(7, 0);
            pt.setInt(8, 1);
            pt.setInt(9, port);

            if (!pt.execute()) {
                System.out.println("게시 성공.");
            }
            pt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("게시 실패.");
            e.printStackTrace();
        }
    }
}
