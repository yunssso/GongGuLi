package back.dao.chatting;

import back.request.board.PostBoardRequest;
import database.DBConnector;
import serverStructure.ChatServer;

import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

public class MakeChattingRoomDAO {
    Connection conn = null;
    PreparedStatement pt = null;

    private static final int MIN_PORT = 7000; //1029 ~ 49151에서 채팅방 서버가 생성됨
    private static final int MAX_PORT = 9000;

    public int makeChattingRoom(int port, PostBoardRequest postBoardRequest) {
        try {
            String masteruuid = postBoardRequest.uuid();
            String title = postBoardRequest.title();
            String region = postBoardRequest.region();
            String category = postBoardRequest.category();
            String maxPeopleNum = postBoardRequest.maxPeopleNum();
            String nowPeopleNum = "0";
            LocalDateTime lastUpdatedTime = LocalDateTime.now();

            conn = DBConnector.getConnection();
            String insertSQL = "INSERT INTO chattingRoom (port, masteruuid, title, region, category, maxpeoplenum, nowpeoplenum, lastupdatedtime)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            pt = conn.prepareStatement(insertSQL);
            pt.setInt(1, port);
            pt.setString(2, masteruuid);
            pt.setString(3, title);
            pt.setString(4, region);
            pt.setString(5, category);
            pt.setString(6, maxPeopleNum);
            pt.setString(7, nowPeopleNum);
            pt.setTimestamp(8, Timestamp.valueOf(lastUpdatedTime));

            if (!pt.execute()) {
                return 1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return 0;
    }

    public int assignChatRoomPort() {
        int port = 0;
        while (true) {
            port = getRandomPortInRange(MIN_PORT, MAX_PORT);

            try {
                ServerSocket serversocket = new ServerSocket(port);

                new ChatServer(serversocket).start();
                break; // 유효한 포트를 찾으면 루프 종료
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return port;
    }

    //    지정해둔 포트 범위 안에서 랜덤한 값을 추출하는 함수
    private static int getRandomPortInRange(int min, int max) {
        try {
            Random random = new Random();
            return random.nextInt(max - min + 1) + min;
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0;
        }
    }
}
