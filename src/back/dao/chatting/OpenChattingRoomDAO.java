package back.dao.chatting;

import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OpenChattingRoomDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    public List<Integer> getAllPorts() {
        List<Integer> ports = new ArrayList<>();
        String selectSQL = "SELECT port FROM board;";

        try {
            conn = DBConnector.getConnection();
            pt = conn.prepareStatement(selectSQL);
            rs = pt.executeQuery();

            while (rs.next()) {
                int port = rs.getInt("port");
                ports.add(port);
            }

            rs.close();
            pt.close();
            conn.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return ports;
    }
}
