package back.dao.chatting;

import back.dao.GetInfoDAO;
import database.DBConnector;
import serverStructure.ChatServer;

import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class JoinChattingRoomDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    public int joinChattingRoom(int port, String uuid) {
        GetInfoDAO getInfoDAO = new GetInfoDAO();
        int nowPeopleNum = getInfoDAO.getNowPeopleNum(port);
        int maxPeopleNum = getInfoDAO.getMaxPeopleNum(port);
        System.out.println(nowPeopleNum);
        System.out.println(maxPeopleNum);
        if (nowPeopleNum == -1 || maxPeopleNum == -1) {
            System.out.println("DB 오류?");
        } else if (nowPeopleNum >= maxPeopleNum) {
            System.out.println("채팅방이 가득 참");
            return 0;   //  채팅방 인원이 가득 참
        } else {
            try {
                conn = DBConnector.getConnection();
                String insertSQL = "INSERT INTO chattingMember (port, memberUuid) VALUES (?, ?);";
                pt = conn.prepareStatement(insertSQL);
                pt.setInt(1, port);
                pt.setString(2, uuid);

                if (!pt.execute()) {
                    pt.close();
                    conn.close();
                    System.out.println("입장 성공");
                    return 1;   //  채팅방 입장 성공
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("에러");
        return -1;  //  에러
    }
}