package back.dao.chatting;

import back.dao.GetInfoDAO;
import back.response.chatroom.GetChattingRoomResponse;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetChatRoomListDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    /*채팅방 리스트 반환*/
    public List<GetChattingRoomResponse> getChattingRoomList(String uuid) {
        List portList = getPortMethod(uuid);
        List<GetChattingRoomResponse> list = new ArrayList<>();
        GetInfoDAO getInfoDAO = new GetInfoDAO();
        try {
            conn = DBConnector.getConnection();
            String selectSQL = "SELECT * FROM chattingRoom WHERE port = ?";

            Iterator iterator = portList.iterator();
            while (iterator.hasNext()) {
                int port = (int) iterator.next();
                pt = conn.prepareStatement(selectSQL);
                pt.setInt(1, port);
                rs = pt.executeQuery();
                while (rs.next()) {
                    GetChattingRoomResponse getChattingRoomResponse = new GetChattingRoomResponse(
                            port,
                            this.rs.getString("region"),
                            this.rs.getString("category"),
                            this.rs.getString("title"),
                            getInfoDAO.getNickNameMethod(rs.getString("masterUuid")),
                            this.rs.getString("lastUpdatedTime")
                    );
                    list.add(getChattingRoomResponse);
                }
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

    public int getInChattingroom(int selectRow, String uuid) {
        try {
            List<GetChattingRoomResponse> chattingRoomList = getChattingRoomList(uuid);
            GetChattingRoomResponse getChattingRoomResponse = chattingRoomList.get(selectRow);
            return getChattingRoomResponse.port();
        } catch (Exception exception ) {
            exception.printStackTrace();
        }

        return -1;
    }

    /*내가 참여해 있는 채팅방의 포트를 저장*/
    public List getPortMethod(String uuid) {
        List portList = new ArrayList();
        try {
            conn = DBConnector.getConnection();
            String selectSQL = "SELECT port FROM chattingMember WHERE memberUuid = ?";
            pt = conn.prepareStatement(selectSQL);
            pt.setString(1, uuid);
            rs = pt.executeQuery();
            while (rs.next()) {
                portList.add(rs.getInt("port"));
            }
            return portList;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
