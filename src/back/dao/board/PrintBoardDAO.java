package back.dao.board;

import back.BoardDTO;
import back.UserDTO;
import back.dao.GetInfoDAO;
import back.request.mypage.MyBoardInfoRequest;
import back.request.mypage.MyHistoryInfoRequest;
import back.response.mypage.MyBoardInfoResponse;
import back.response.mypage.MyHistoryInfoResponse;
import database.DBConnector;
import back.response.board.BoardInfoResponse;

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
    public List<BoardInfoResponse> printBoard(String region, String category) {
        List<BoardInfoResponse> list = new ArrayList<>();

        try {
            conn = DBConnector.getConnection();
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
                String peoplenum = rs.getInt("nowPeopleNum") + "/" + rs.getString("maxPeopleNum");

                PreparedStatement pt1 = conn.prepareStatement(nickNameSQL);
                pt1.setString(1, rs.getString("uuid"));
                ResultSet rs2 = pt1.executeQuery();
                rs2.next();

                BoardInfoResponse boardInfoResponse = new BoardInfoResponse(
                        this.rs.getString("region"),
                        this.rs.getString("category"),
                        this.rs.getString("title"),
                        rs2.getString(1),
                        peoplenum
                );
                list.add(boardInfoResponse);

                pt1.close();
                rs2.close();
            }
            rs.close();
            pt.close();
            conn.close();

            return list;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    // 내가 쓴 게시글 출력 (마이페이지)
    public List<MyBoardInfoResponse> printMyBoard(String uuid) {
        List<MyBoardInfoResponse> list = new ArrayList<>();

        try {
            conn = DBConnector.getConnection();
            String selectSQL = "SELECT * FROM board WHERE uuid = ? ORDER BY postingTime DESC;";

            pt = conn.prepareStatement(selectSQL);
            pt.setString(1, uuid);
            rs = pt.executeQuery();

            while (rs.next()) {
                String peoplenum = rs.getInt("nowPeopleNum") + "/" + rs.getString("maxPeopleNum");

                MyBoardInfoResponse myBoardInfoResponse = new MyBoardInfoResponse(
                        rs.getString("region"),
                        rs.getString("category"),
                        rs.getString("title"),
                        peoplenum
                );

                list.add(myBoardInfoResponse);
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

    // 내가 참여한 공동구매 (마이페이지)
    public List<MyHistoryInfoResponse> printMyHistoryBoard(String uuid) {
        // 역순으로 리스트에 담기
        List<MyHistoryInfoResponse> list = new ArrayList<>();
        conn = DBConnector.getConnection();
        List<Integer> myHistoryPort = getMyHistoryPort(uuid);
        if (myHistoryPort != null) {
            try {
                GetInfoDAO getInfoDAO = new GetInfoDAO();
                for (int i = 0; i < myHistoryPort.size(); i++) {
                    int port = myHistoryPort.get(i);
                    String selectSQL = "SELECT * FROM board WHERE port = ?;";

                    pt = conn.prepareStatement(selectSQL);
                    pt.setInt(1, port);
                    rs = pt.executeQuery();

                    if (rs.next()) {
                        String peoplenum = rs.getInt("nowPeopleNum") + "/" + rs.getString("maxPeopleNum");
                        String writer = getInfoDAO.getnickNameMethod(rs.getString("uuid"));

                        MyHistoryInfoResponse myHistoryInfoResponse = new MyHistoryInfoResponse(
                                rs.getString("region"),
                                rs.getString("category"),
                                rs.getString("title"),
                                writer,
                                peoplenum
                        );

                        list.add(myHistoryInfoResponse);
                    }
                }

                rs.close();
                pt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("내 글 ArrayList 저장 중 오류 발생.");
            }
        }
        return list;
    }

    public ArrayList<Integer> getMyHistoryPort(String uuid) {
        ArrayList<Integer> portList = new ArrayList<Integer>();
        try {
            String getPortSQL = "SELECT port FROM chattingmember WHERE memberUuid = ?";
            pt = conn.prepareStatement(getPortSQL);
            pt.setString(1, uuid);
            rs = pt.executeQuery();
            while (rs.next()) {
                int port = rs.getInt(1);
                portList.add(port);
            }
        } catch (Exception e) {
            e.printStackTrace();
            portList = null;
        }
        return portList;
    }
}