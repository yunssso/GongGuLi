package back.dao.board;

import back.request.board.DeleteBoardRequest;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DeleteBoardDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;
    public boolean deleteBoardMethod(int port) {
        boolean isDeleted = false;
        String deleteSQL = "DELETE FROM board WHERE port = ?";
        try {
            conn = DBConnector.getConnection();
            pt = conn.prepareStatement(deleteSQL);
            pt.setInt(1, port);

            if (!pt.execute()) {
                isDeleted = true;
                System.out.println("게시글 삭제 성공.");
            }
            pt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeleted;
    }
}
