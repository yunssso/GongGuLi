package back.dao;

import back.BoardDTO;
import back.UserDTO;
import database.DBConnector;
import back.response.board.Board_Info_Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrintBoardDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    // 게시판 출력
    public List<Board_Info_Response> printBoard(String region, String category) {
        List<Board_Info_Response> list = new ArrayList<>();
        conn = DBConnector.getConnection();

        try {
            String selectSQL;
            if (region.equals(" --") && !category.equals(" --")) {
                selectSQL = "SELECT * FROM board WHERE category = ? ORDER BY postingTime DESC;";
                pt = conn.prepareStatement(selectSQL);
                pt.setString(1, category);
            } else if (!region.equals(" --") && category.equals(" --")) {
                selectSQL = "SELECT * FROM board WHERE region = ? ORDER BY postingTime DESC;";
                pt = conn.prepareStatement(selectSQL);
                pt.setString(1, region);
            } else if (!region.equals(" --") && !category.equals(" --")) {
                selectSQL = "SELECT * FROM board WHERE region = ? AND category = ? ORDER BY postingTime DESC;";
                pt = conn.prepareStatement(selectSQL);
                pt.setString(1, region);
                pt.setString(2, category);
            } else {
                selectSQL = "SELECT * FROM board ORDER BY postingTime DESC";
                pt = conn.prepareStatement(selectSQL);
            }

            rs = pt.executeQuery();
            String nickNameSQL = "SELECT nickName FROM user WHERE uuid = ?";

            while (rs.next()) {
                String peoplenum = rs.getInt("nowPeopleNum") + "/" + rs.getString("peopleNum");

                PreparedStatement pt2 = conn.prepareStatement(nickNameSQL);
                pt2.setString(1, rs.getString("uuid"));
                ResultSet rs2 = pt2.executeQuery();
                rs2.next();

                Board_Info_Response boardInfoResponse = new Board_Info_Response(
                        this.rs.getString("region"),
                        this.rs.getString("category"),
                        this.rs.getString("title"),
                        rs2.getString(1),
                        peoplenum
                        );
                list.add(boardInfoResponse);

                pt2.close();
                rs2.close();
            }
            rs.close();
            pt.close();
            conn.close();

            return list;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    // 내가 쓴 게시글 출력 (마이페이지)
    public String[][] printMyBoard(UserDTO userDTO) {
        // 역순으로 리스트에 담기
        List<BoardDTO> list = new ArrayList<>();
        conn = DBConnector.getConnection();
        String selectSQL = "SELECT * FROM board WHERE nickName = ? ORDER BY postingTime DESC;";
        try {
            pt = conn.prepareStatement(selectSQL);
            pt.setString(1, userDTO.getNickName());
            rs = pt.executeQuery();
            while (rs.next()) {
                String peoplenum = rs.getInt("nowPeopleNum") + "/" + rs.getString("peopleNum");

                /*BoardDTO boardDTO = new BoardDTO();
                boardDTO.setTitle(rs.getString("title"));
                boardDTO.setRegion(rs.getString("region"));
                boardDTO.setCategory(rs.getString("category"));
                boardDTO.setNickName(rs.getString("nickName"));
                boardDTO.setPeopleNum(peoplenum);
                boardDTO.setContent(rs.getString("content"));

                list.add(boardDTO);*/
            }
            System.out.println("내 글 데이터 ArrayList에 저장 완료.");

            rs.close();
            pt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("내 글 ArrayList 저장 중 오류 발생.");
        }

        String[][] data = new String[list.size()][];    // ArrayList에 저장한 데이터들 2차원 배열로 변환해주기.

        for (int i = 0; i < list.size(); i++) {
            BoardDTO boardDTO = list.get(i);
            data[i] = new String[]{boardDTO.getRegion(), boardDTO.getCategory(), boardDTO.getTitle(), boardDTO.getPeopleNum()};
        }
        // 지역 카테고리 제목 현황
        System.out.println("내 글 2차원 배열로 변환 완료.");

        return data;
    }

    // 내가 참여한 공동구매 (마이페이지)
    public String[][] printMyHistoryBoard(UserDTO userDTO) {
        // 역순으로 리스트에 담기
        List<BoardDTO> list = new ArrayList<>();
        conn = DBConnector.getConnection();
        String selectSQL = "SELECT * FROM board WHERE nickName = ? ORDER BY postingTime DESC;";
        try {
            pt = conn.prepareStatement(selectSQL);
            pt.setString(1, userDTO.getNickName());
            rs = pt.executeQuery();
            while (rs.next()) {
                String peoplenum = rs.getInt("nowPeopleNum") + "/" + rs.getString("peopleNum");

                /*BoardDTO boardDTO = new BoardDTO();
                boardDTO.setTitle(rs.getString("title"));
                boardDTO.setRegion(rs.getString("region"));
                boardDTO.setCategory(rs.getString("category"));
                boardDTO.setNickName(rs.getString("nickName"));
                boardDTO.setPeopleNum(peoplenum);
                boardDTO.setContent(rs.getString("content"));

                list.add(boardDTO);*/
            }
            System.out.println("내 글 데이터 ArrayList에 저장 완료.");

            rs.close();
            pt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("내 글 ArrayList 저장 중 오류 발생.");
        }

        String[][] data = new String[list.size()][];    // ArrayList에 저장한 데이터들 2차원 배열로 변환해주기.

        for (int i = 0; i < list.size(); i++) {
            BoardDTO boardDTO = list.get(i);
            data[i] = new String[]{boardDTO.getRegion(), boardDTO.getCategory(), boardDTO.getTitle(), boardDTO.getNickName(), boardDTO.getPeopleNum()};
        }

        System.out.println("내가 참여한 공구 내역 출력 완료.");

        return data;
    }
}
