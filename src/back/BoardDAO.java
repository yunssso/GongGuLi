package back;

import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;
    public String[][] selectAll() {
        List<BoardDTO> list = new ArrayList<>();
        conn = DBConnector.getConnection();
        String selectSQL = "SELECT * FROM board ORDER BY postingTime DESC"; // 역순으로 리스트에 담기
        try {
            pt = conn.prepareStatement(selectSQL);
            rs = pt.executeQuery();
            while (rs.next()) {
                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setTitle(rs.getString("title"));
                boardDTO.setRegion(rs.getString("region"));
                boardDTO.setCategory(rs.getString("category"));
                boardDTO.setNickName(rs.getString("nickName"));
                boardDTO.setPeopleNum(rs.getString("peopleNum"));
                boardDTO.setContent(rs.getString("content"));

                list.add(boardDTO);
            }
            System.out.println("데이터 ArrayList에 저장 완료.");

            rs.close();
            pt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("ArrayList 저장 중 오류 발생.");
        }

        String[][] data = new String[list.size()][];    // ArrayList에 저장한 데이터들 2차원 배열로 변환해주기.

        for (int i = 0; i < list.size(); i++) {
            BoardDTO dto = list.get(i);
            data[i] = new String[]{dto.getRegion(), dto.getCategory(), dto.getTitle(), dto.getNickName(), dto.getPeopleNum()};
        }
        System.out.println("2차원 배열로 변환 완료.");

        return data;
    }

    public BoardDTO readMorePost(int selectRow) {   // 게시글 자세히 보기
        selectRow++;
        BoardDTO boardDTO = new BoardDTO();
        String selectSQL = "SELECT * FROM boardView WHERE num = ?";
        String updateSQL = "UPDATE board SET view = view + 1 WHERE title = ? AND region = ? AND category = ? AND writer = ? AND peopleNum = ? AND content = ?";
        try {
            conn = DBConnector.getConnection();
            pt = conn.prepareStatement(selectSQL);
            pt.setInt(1, selectRow);
            rs = pt.executeQuery();
            if (rs.next()) {
                boardDTO.setTitle(rs.getString("title"));
                boardDTO.setRegion(rs.getString("region"));
                boardDTO.setCategory(rs.getString("category"));
                boardDTO.setNickName(rs.getString("nickName"));
                boardDTO.setPeopleNum(rs.getString("peopleNum"));
                boardDTO.setContent(rs.getString("content"));
                boardDTO.setView(rs.getInt("view") + 1);
            }
            System.out.println("자세히 보기 성공.");

            pt = conn.prepareStatement(updateSQL);
            pt.setString(1, boardDTO.getTitle());
            pt.setString(2, boardDTO.getRegion());
            pt.setString(3, boardDTO.getCategory());
            pt.setString(4, boardDTO.getNickName());
            pt.setString(5, boardDTO.getPeopleNum());
            pt.setString(6, boardDTO.getContent());
            pt.execute();

            rs.close();
            pt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            conn = DBConnector.getConnection();


            System.out.println("조회수 1 증가");

            pt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boardDTO;
    }

    public void posting(BoardDTO boardDTO) {
        conn = DBConnector.getConnection();
        String insertSQL = "INSERT INTO board(title, region, category, peopleNum, content, nickName, view) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            pt = conn.prepareStatement(insertSQL);
            pt.setString(1, boardDTO.getTitle());
            pt.setString(2, boardDTO.getRegion());
            pt.setString(3, boardDTO.getCategory());
            pt.setString(4, boardDTO.getPeopleNum());
            pt.setString(5, boardDTO.getContent());
            pt.setString(6, boardDTO.getNickName());
            pt.setInt(7, 0);

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
