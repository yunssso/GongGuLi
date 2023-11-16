package back.dao;

import back.dto.FindUserIdDto;
import back.dto.FindUserPasswordDto;
import back.dto.LoginDto;
import back.dto.SignUpDto;
import back.UserDTO;
import database.DBConnector;
import front.MainPage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    public void signUp(SignUpDto signUpInfo, String uuid) {
        conn = DBConnector.getConnection();
        String signInSQL = "INSERT INTO user (nickName, name, userId, password, region, phoneNum, birth) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            pt = conn.prepareStatement(signInSQL);

            pt.setString(1, signUpInfo.nickName());
            pt.setString(2, signUpInfo.name());
            pt.setString(3, signUpInfo.userId());
            pt.setString(4, signUpInfo.password());
            pt.setString(5, signUpInfo.region());
            pt.setString(6, signUpInfo.phoneNumber());
            pt.setString(7, signUpInfo.birth());

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

    public String logInCheck(LoginDto loginDto) {
        String a;
        conn = DBConnector.getConnection();
        String checkSQL = "SELECT password FROM user WHERE userId = ?";
        try {
            pt = conn.prepareStatement(checkSQL);
            pt.setString(1, loginDto.userId());
            rs = pt.executeQuery();
            if (rs.next()) {
                if (rs.getString(1).equals(loginDto.password())) {
                    a = "1";   // 로그인 성공 //여기서 "1"을 빼고 uuid를 a 안에 넣어줘야 돼.
                } else {
                    a = "0";   // 비밀번호 불일치
                }
            } else {
                a = "-1";   // 아이디가 존재하지 않음
            }

            rs.close();
            pt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            a = "-2";  // DB 오류
        }
        return a;
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

    public String findID(FindUserIdDto findUserIdInfo) {
        conn = DBConnector.getConnection();
        String selectsql = "select userId from user where name = ? and birth = ? and phoneNum= ?;";
        try {
            pt = conn.prepareStatement(selectsql);
            pt.setString(1, findUserIdInfo.name());
            pt.setString(2, findUserIdInfo.birth());
            pt.setString(3, findUserIdInfo.phoneNumber());
            rs = pt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return "";
    }

    public String findPassword(FindUserPasswordDto findUserPasswordDto) {
        conn = DBConnector.getConnection();
        String selectsql = "select password from user where name = ? and userID = ? and birth = ? and phoneNum= ?;";
        try {
            pt = conn.prepareStatement(selectsql);
            pt.setString(1, findUserPasswordDto.name());
            pt.setString(2, findUserPasswordDto.userId());
            pt.setString(3, findUserPasswordDto.birth());
            pt.setString(4, findUserPasswordDto.phoneNumber());
            rs = pt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return "";
    }
}
