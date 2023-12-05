package back.dao.board;

import back.request.board.DeleteBoardRequest;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
            System.out.println("게시글 삭제 성공.");

            deleteSQL = "DELETE FROM chattingRoom WHERE port = ?;";
            pt = conn.prepareStatement(deleteSQL);
            pt.setInt(1, port);
            pt.execute();
            System.out.println("채팅방 삭제 성공.");

            deleteSQL = "DELETE FROM chattingMember WHERE port = ?;";
            pt = conn.prepareStatement(deleteSQL);
            pt.setInt(1, port);
            pt.execute();
            System.out.println("채팅방 입장 내역 삭제 완료.");

            deleteSQL = "DELETE FROM chattingMessage WHERE port = ?;";
            pt = conn.prepareStatement(deleteSQL);
            pt.setInt(1, port);
            pt.execute();
            System.out.println("채팅 메세지 삭제 완료.");

            pt.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
