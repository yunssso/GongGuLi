package back.dao;

import back.BoardDTO;
import back.UserDTO;
import back.request.Post_Board_Request;
import database.DBConnector;
import back.response.Board_Info_Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {
    Connection conn = null;
    PreparedStatement pt1 = null;
    ResultSet rs1 = null;

    // arraylist에 역순으로 담은 뒤, 2차원 배열로 저장 및 전달
    // 전체 개시글
    // 이 코드 기준으로 밑에 코드들 다 바꿔야 됨 ( 고민재 할 일 )
    public List<Board_Info_Response> printBoard(String region, String category, String uuid) {
        List<Board_Info_Response> list = new ArrayList<>();
        conn = DBConnector.getConnection();

        try {
            String selectSQL;
            if (region.equals(" --") && !category.equals(" --")) {
                selectSQL = "SELECT * FROM board WHERE category = ? ORDER BY postingTime DESC;";
                pt1 = conn.prepareStatement(selectSQL);
                pt1.setString(1, category);
            } else if (!region.equals(" --") && category.equals(" --")) {
                selectSQL = "SELECT * FROM board WHERE region = ? ORDER BY postingTime DESC;";
                pt1 = conn.prepareStatement(selectSQL);
                pt1.setString(1, region);
            } else if (!region.equals(" --") && !category.equals(" --")) {
                selectSQL = "SELECT * FROM board WHERE region = ? AND category = ? ORDER BY postingTime DESC;";
                pt1 = conn.prepareStatement(selectSQL);
                pt1.setString(1, region);
                pt1.setString(2, category);
            } else {
                selectSQL = "SELECT * FROM board ORDER BY postingTime DESC";
                pt1 = conn.prepareStatement(selectSQL);
            }

            rs1 = pt1.executeQuery();
            String nickNameSQL = "SELECT nickName FROM user WHERE uuid = ?";

            while (rs1.next()) {
                String peoplenum = rs1.getInt("nowPeopleNum") +"/"+ rs1.getString("peopleNum");

                PreparedStatement pt2 = conn.prepareStatement(nickNameSQL);
                pt2.setString(1, rs1.getString("uuid"));
                ResultSet rs2 = pt2.executeQuery();
                rs2.next();

                Board_Info_Response boardInfoResponse = new Board_Info_Response(
                        rs1.getString("title"),
                        rs1.getString("region"),
                        rs1.getString("category"),
                        rs1.getString("peopleNum"),
                        rs1.getString("content"),
                        rs1.getString("uuid").equals(uuid)
                );

                list.add(boardInfoResponse);

                pt2.close();
                rs2.close();
            }
            System.out.println("데이터 ArrayList에 저장 완료.");

            rs1.close();
            pt1.close();
            conn.close();

            return list;
        } catch (SQLException e) {
            System.out.println("ArrayList 저장 중 오류 발생.");
            return list = null; //오류 발생하면 list를 null로 만들고 반환 해주도록 함
        }
    }

    // 내가 쓴 게시글 (마이페이지)
    public String[][] printMyBoard(UserDTO userDTO) {
        // 역순으로 리스트에 담기
        List<BoardDTO> list = new ArrayList<>();
        conn = DBConnector.getConnection();
        String selectSQL = "SELECT * FROM board WHERE nickName = ? ORDER BY postingTime DESC;";
        try {
            pt1 = conn.prepareStatement(selectSQL);
            pt1.setString(1, userDTO.getNickName());
            rs1 = pt1.executeQuery();
            while (rs1.next()) {
                String peoplenum = rs1.getInt("nowPeopleNum") +"/"+ rs1.getString("peopleNum");

                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setTitle(rs1.getString("title"));
                boardDTO.setRegion(rs1.getString("region"));
                boardDTO.setCategory(rs1.getString("category"));
                boardDTO.setNickName(rs1.getString("nickName"));
                boardDTO.setPeopleNum(peoplenum);
                boardDTO.setContent(rs1.getString("content"));

                list.add(boardDTO);
            }
            System.out.println("내 글 데이터 ArrayList에 저장 완료.");

            rs1.close();
            pt1.close();
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
            pt1 = conn.prepareStatement(selectSQL);
            pt1.setString(1, userDTO.getNickName());
            rs1 = pt1.executeQuery();
            while (rs1.next()) {
                String peoplenum = rs1.getInt("nowPeopleNum") +"/"+ rs1.getString("peopleNum");

                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setTitle(rs1.getString("title"));
                boardDTO.setRegion(rs1.getString("region"));
                boardDTO.setCategory(rs1.getString("category"));
                boardDTO.setNickName(rs1.getString("nickName"));
                boardDTO.setPeopleNum(peoplenum);
                boardDTO.setContent(rs1.getString("content"));

                list.add(boardDTO);
            }
            System.out.println("내 글 데이터 ArrayList에 저장 완료.");

            rs1.close();
            pt1.close();
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

    // 메인 게시판에서 게시글 자세히 보기
    public BoardDTO readMorePost(int selectRow) {   // 게시글 자세히 보기
        selectRow++;
        BoardDTO boardDTO = new BoardDTO();
        String selectSQL = "SELECT * FROM boardView WHERE num = ?";
        String updateSQL = "UPDATE board SET view = view + 1 WHERE boardID = ?";
        try {
            conn = DBConnector.getConnection();
            pt1 = conn.prepareStatement(selectSQL);
            pt1.setInt(1, selectRow);
            rs1 = pt1.executeQuery();
            if (rs1.next()) {
                String peoplenum = rs1.getInt("nowPeopleNum") +"/"+ rs1.getString("peopleNum");

                boardDTO.setBoardId(rs1.getInt("boardID"));
                boardDTO.setTitle(rs1.getString("title"));
                boardDTO.setRegion(rs1.getString("region"));
                boardDTO.setCategory(rs1.getString("category"));
                boardDTO.setNickName(rs1.getString("nickName"));
                boardDTO.setPeopleNum(peoplenum);
                boardDTO.setContent(rs1.getString("content"));
                boardDTO.setView(rs1.getInt("view") + 1);
            }
            System.out.println("자세히 보기 성공.");

            pt1 = conn.prepareStatement(updateSQL);
            pt1.setInt(1, boardDTO.getBoardId());
            pt1.execute();

            rs1.close();
            pt1.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            conn = DBConnector.getConnection();
            System.out.println("조회수 1 증가");
            pt1.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boardDTO;
    }

    // 마이페이지에서 내가 쓴 글 자세히 보기
    public BoardDTO readMoreMyPost(int selectRow) {   // 게시글 자세히 보기
        selectRow++;
        BoardDTO boardDTO = new BoardDTO();
        String selectSQL = "SELECT * FROM boardView WHERE num = ?";
        try {
            conn = DBConnector.getConnection();
            pt1 = conn.prepareStatement(selectSQL);
            pt1.setInt(1, selectRow);
            rs1 = pt1.executeQuery();
            if (rs1.next()) {
                String peoplenum = rs1.getInt("nowPeopleNum") +"/"+ rs1.getString("peopleNum");

                boardDTO.setBoardId(rs1.getInt("boardID"));
                boardDTO.setTitle(rs1.getString("title"));
                boardDTO.setRegion(rs1.getString("region"));
                boardDTO.setCategory(rs1.getString("category"));
                boardDTO.setNickName(rs1.getString("nickName"));
                boardDTO.setPeopleNum(peoplenum);
                boardDTO.setContent(rs1.getString("content"));
                boardDTO.setView(rs1.getInt("view") + 1);
            }
            System.out.println("자세히 보기 성공.");

            rs1.close();
            pt1.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boardDTO;
    }
    public void posting(Post_Board_Request Post_BoardInfo) {
        conn = DBConnector.getConnection();
        String insertSQL = "INSERT INTO board(title, region, category, peopleNum, content, uuid, view, nowPeopleNum) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pt1 = conn.prepareStatement(insertSQL);
            pt1.setString(1, Post_BoardInfo.title());
            pt1.setString(2, Post_BoardInfo.region());
            pt1.setString(3, Post_BoardInfo.category());
            pt1.setString(4, Post_BoardInfo.peopleNum());
            pt1.setString(5, Post_BoardInfo.content());
            pt1.setString(6, Post_BoardInfo.uuid());   // <- 이 부분에 닉네임 대신에 UUID 값이 들어갈 거 같은데?
            pt1.setInt(7, 0);
            pt1.setInt(8, 1);

            if (!pt1.execute()) {
                System.out.println("게시 성공.");
            }

            pt1.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("게시 실패.");
            e.printStackTrace();
        }
    }
}
