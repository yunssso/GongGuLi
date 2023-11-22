package back.dao;

import back.request.account.Find_UserId_Request;
import back.request.account.Find_UserPassword_Request;
import back.request.account.Login_Request;
import back.request.account.SignUp_Request;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    public void signUp(SignUp_Request signUpInfo, String uuid) {
        conn = DBConnector.getConnection();
        String signInSQL = "INSERT INTO user (nickName, name, userId, password, region, phoneNum, birth, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pt = conn.prepareStatement(signInSQL);

            pt.setString(1, signUpInfo.nickName());
            pt.setString(2, signUpInfo.name());
            pt.setString(3, signUpInfo.userId());
            pt.setString(4, signUpInfo.password());
            pt.setString(5, signUpInfo.region());
            pt.setString(6, signUpInfo.phoneNumber());
            pt.setString(7, signUpInfo.birth());
            pt.setString(8, uuid);

            if (!pt.execute()) {
                System.out.println("회원가입 완료.");
            }

            pt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("회원가입 실패.");
            e.printStackTrace();
        }
    }

    public String logInCheck(Login_Request loginRequest) {
        String getUuid;
        conn = DBConnector.getConnection();
        String checkSQL = "SELECT password FROM user WHERE userId = ?";
        String getSQL = "SELECT uuid FROM user WHERE userId = ?";
        try {
            pt = conn.prepareStatement(checkSQL);
            pt.setString(1, loginRequest.userId());
            rs = pt.executeQuery();
            if (rs.next()) {
                if (rs.getString(1).equals(loginRequest.password())) {
                    pt = conn.prepareStatement(getSQL);
                    pt.setString(1, loginRequest.userId());
                    rs = pt.executeQuery();
                    rs.next();
                    getUuid = rs.getString(1);   // 로그인 성공
                } else {
                    getUuid = "Password Does Not Match";   // 비밀번호 불일치
                }
            } else {
                getUuid = "Id Does Not Exist";   // 아이디가 존재하지 않음
            }

            rs.close();
            pt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            getUuid = "Database Error";  // DB 오류
        }
        System.out.println(getUuid);
        return getUuid;
    }

    public void modifyUserInfo() {

    }

    public boolean nickNameCheck(String inpNickName) {
        boolean nickNameCheck = false;
        conn = DBConnector.getConnection();
        String existssql = "SELECT exists(select nickName from user where nickName = ?) as cnt;";
        try {
            pt = conn.prepareStatement(existssql);
            pt.setString(1, inpNickName);
            rs = pt.executeQuery();
            if (rs.next()) {
                int cnt = rs.getInt("cnt");
                System.out.println(cnt);
                if (cnt == 0) {
                    nickNameCheck = true;
                }
            }
        } catch (Exception e) {
            System.out.println("nickNameCheck Error");
        }

        return nickNameCheck;
    }
    }
