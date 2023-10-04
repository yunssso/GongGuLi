package database;

import java.sql.Connection;
import java.sql.DriverManager;

// DB에 접속하는 코드
public class DBConnector {
    private static String driver = "org.mariadb.jdbc.Driver";  // jdbc 드라이버.
    private static String url = "jdbc:mariadb://localhost:3306/test";      // DB 서버 주소 ( 이 주소는 로컬 주소 ) ( 나중에 바꿔야됨 )
    private static String user = "root";       // 서버에 접속하는 유저 이름 ( 서버에서 권한 주는 식인듯? )
    private static String password = "123456";     // 서버 비밀번호

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Successfully connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
