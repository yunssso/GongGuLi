package back.dao.user;

import back.dao.CheckDAO;
import back.request.account.LoginRequest;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LogInDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;
    private final CheckDAO checkDAO = new CheckDAO();

    public String logIn(LoginRequest loginRequest) {
        String logInResult = "Database or SQL Error";       // DB 또는 logInCheck에서의 SQL문 오류
        String getSQL = "SELECT uuid FROM user WHERE userId = ?";
        int logInCheckResult = checkDAO.logInCheck(loginRequest.userId(), loginRequest.password());
        try {
            if (logInCheckResult == 1) {
                conn = DBConnector.getConnection();
                pt = conn.prepareStatement(getSQL);
                pt.setString(1, loginRequest.userId());
                rs = pt.executeQuery();
                rs.next();
                logInResult = rs.getString(1);   // 로그인 성공
                pt.close();
                conn.close();
            } else if (logInCheckResult == 2) {
                logInResult = "Password Does Not Match";        // 비밀번호 틀림
            } else if (logInCheckResult == 3) {
                logInResult = "Id Does Not Exist";      // 아이디가 존재하지 않음
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logInResult;
    }
}
