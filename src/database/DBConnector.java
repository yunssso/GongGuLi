package database;

import java.sql.Connection;
import java.sql.DriverManager;

// DB에 접속하는 코드
public class DBConnector {
    private static String driver = "org.mariadb.jdbc.Driver";  // jdbc 드라이버.
    private static String user = "root";       // 서버에 접속하는 유저 이름 ( 서버에서 권한 주는 식인듯? )
<<<<<<< HEAD
//    private static String url = "jdbc:mariadb://43.200.49.16:3306/GongGuLi";      // DB 서버 주소 ( 이 주소는 로컬 주소 )
        private static String url = "jdbc:mariadb://gongguli.c5bclsnfmtbe.ap-northeast-2.rds.amazonaws.com:3306/GongGuLi";      // RDS DB 서버 주소
//    private static String password = "123456";     // 서버 비밀번호
        private static String password = "root1234";     // RDS 서버 비밀번호

     public static Connection getConnection() {
         Connection con = null;
         try {
             Class.forName(driver);
             con = DriverManager.getConnection(url, user, password);
         } catch (Exception e) {
        e.printStackTrace();
=======
//    private static String url = "jdbc:mariadb://localhost:3306/GongGuLi";      // DB 서버 주소 ( 이 주소는 로컬 주소 )
            private static String url = "jdbc:mariadb://gongguli.c5bclsnfmtbe.ap-northeast-2.rds.amazonaws.com:3306/GongGuLi";      // RDS DB 서버 주소
//    private static String password = "123456";     // 서버 비밀번호
        private static String password = "root1234";     // RDS 서버 비밀번호

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
>>>>>>> 80e0807bc1312bef7cd5e6dd978799af91567b64
        }
        return con;
    }
}
