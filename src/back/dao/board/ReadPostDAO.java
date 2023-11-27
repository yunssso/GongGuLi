package back.dao.board;

import back.response.board.BoardInfoMoreResponse;
import back.response.mypage.MyBoardInfoMoreResponse;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReadPostDAO {
    Connection conn = null;
    PreparedStatement pt = null;

    ResultSet rs = null;
//    게시글 자세히 보기 (메인페이지)
    public BoardInfoMoreResponse readMorePost(int selectRow, String uuid) {
        selectRow++;

        String selectSQL = "SELECT * FROM boardView WHERE num = ?;";
        String nickNameSQL = "SELECT nickName FROM user WHERE uuid = ?;";
        String updateSQL = "UPDATE board SET view = view + 1 WHERE port = ?;";

        try {
            conn = DBConnector.getConnection();
            pt = conn.prepareStatement(selectSQL);
            pt.setInt(1, selectRow);
            rs = pt.executeQuery();
            pt = conn.prepareStatement(nickNameSQL);
            rs.next();
            pt.setString(1, rs.getString("uuid"));
            ResultSet rs1 = pt.executeQuery();

            if (rs1.next()) {
                String peoplenum = rs.getInt("nowPeopleNum") + "/" + rs.getString("maxPeopleNum");

                BoardInfoMoreResponse boardInfoMoreResponse = new BoardInfoMoreResponse(
                        rs.getInt("port"),
                        rs.getString("title"),
                        rs.getString("region"),
                        rs.getString("category"),
                        rs1.getString(1),
                        peoplenum,
                        rs.getString("content"),
                        rs.getInt("view") + 1,
                        rs.getString("uuid").equals(uuid)
                );

                pt = conn.prepareStatement(updateSQL);
                pt.setInt(1, boardInfoMoreResponse.port());
                pt.execute();

                rs.close();
                rs1.close();
                pt.close();
                conn.close();

                return boardInfoMoreResponse;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

//    내가 쓴 글 자세히 보기 (마이페이지)
    public MyBoardInfoMoreResponse readMoreMyPost(int selectRow) {
    selectRow++;
    String selectSQL = "SELECT * FROM boardView WHERE num = ?";
    String updateSQL = "UPDATE board SET view = view + 1 WHERE port = ?;";
    try {
        conn = DBConnector.getConnection();
        pt = conn.prepareStatement(selectSQL);
        pt.setInt(1, selectRow);
        rs = pt.executeQuery();
        if (rs.next()) {
            String peoplenum = rs.getInt("nowPeopleNum") +"/"+ rs.getString("maxPeopleNum");

            MyBoardInfoMoreResponse myBoardInfoMoreResponse = new MyBoardInfoMoreResponse(
                    rs.getInt("port"),
                    rs.getString("title"),
                    rs.getString("region"),
                    rs.getString("category"),
                    peoplenum,
                    rs.getString("content"),
                    rs.getInt("view" + 1)
            );
            pt = conn.prepareStatement(updateSQL);
            pt.setInt(1, myBoardInfoMoreResponse.port());
            pt.execute();
            System.out.println("자세히 보기 성공.");

            rs.close();
            pt.close();
            conn.close();

            return myBoardInfoMoreResponse;
        }
        rs.close();
        pt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
}
