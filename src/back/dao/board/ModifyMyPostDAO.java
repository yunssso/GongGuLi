package back.dao.board;

import back.request.board.ModifyMyPostRequest;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ModifyMyPostDAO {
    Connection conn = null;
    PreparedStatement pt = null;

    public boolean modifyMyPost(ModifyMyPostRequest modifyMyPostRequest) {
        boolean isModified = false;
        try {
            conn = DBConnector.getConnection();
            String updateSQL = "UPDATE board SET title = ?, region = ?, category = ?, content = ? WHERE port = ?;";
            pt = conn.prepareStatement(updateSQL);
            pt.setString(1, modifyMyPostRequest.title());
            pt.setString(2, modifyMyPostRequest.region());
            pt.setString(3, modifyMyPostRequest.category());
            pt.setString(4, modifyMyPostRequest.content());
            pt.setInt(5, modifyMyPostRequest.port());
            if (pt.executeUpdate() > 0) {
                isModified = true;
                System.out.println("게시글 수정 성공.");
            }
            pt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isModified;
    }
}
