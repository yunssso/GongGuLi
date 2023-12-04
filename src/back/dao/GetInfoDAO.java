package back.dao;

import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetInfoDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    public String getNickNameMethod(String uuid) {
        try {
            conn = DBConnector.getConnection();
            String nickNameSQL = "SELECT nickName FROM user WHERE uuid = ?;";
            pt = conn.prepareStatement(nickNameSQL);
            pt.setString(1, uuid);
            rs = pt.executeQuery();

            if (rs.next()) {
                String nickName = rs.getString("nickName");

                rs.close();
                pt.close();
                conn.close();

                return nickName;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public String getUuidMethod(String nickName) {
        try {
            conn = DBConnector.getConnection();
            String uuidSQL = "SELECT uuid FROM user WHERE nickName = ?;";
            pt = conn.prepareStatement(uuidSQL);
            pt.setString(1, nickName);
            rs = pt.executeQuery();

            if (rs.next()) {
                String uuid = rs.getString("uuid");

                rs.close();
                pt.close();
                conn.close();

                return uuid;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public int getchatPortMethod(int selectRow) {
        selectRow++;

        try {
            conn = DBConnector.getConnection();
            String selectSQL = "SELECT chatPort FROM boardView WHERE num = ?;";
            pt = conn.prepareStatement(selectSQL);
            pt.setInt(1, selectRow);
            rs = pt.executeQuery();

            if (rs.next()) {
                int chatPort = rs.getInt("chatPort");

                rs.close();
                pt.close();
                conn.close();

                return chatPort;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1; // chatPort를 가져오지 못했을 때의 반환값, 적절한 값으로 변경 필요
    }

    //    채팅방의 현재 인원수를 가져오는 함수
    public int getNowPeopleNum(int port) {
        try {
            conn = DBConnector.getConnection();
            String selectSQL = "SELECT nowPeopleNum FROM chattingRoom WHERE port = ?;";
            pt = conn.prepareStatement(selectSQL);
            pt.setInt(1, port);
            rs = pt.executeQuery();
            if (rs.next()) {
                int nowPeopleNum = Integer.parseInt(rs.getString(1));

                rs.close();
                pt.close();
                conn.close();

                return nowPeopleNum;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int getMaxPeopleNum(int port) {
        try {
            conn = DBConnector.getConnection();
            String selectSQL = "SELECT maxPeopleNum FROM chattingRoom WHERE port = ?;";
            pt = conn.prepareStatement(selectSQL);
            pt.setInt(1, port);
            rs = pt.executeQuery();
            if (rs.next()) {
                int maxPeopleNum = Integer.parseInt(rs.getString(1));

                rs.close();
                pt.close();
                conn.close();

                return maxPeopleNum;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}