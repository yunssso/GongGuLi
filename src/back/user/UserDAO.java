package back.user;

import database.DBConnector;
import front.MainPage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    Connection conn = null;
    PreparedStatement pt = null;
    ResultSet rs = null;

    public int logInCheck(String inpId, String inpPassword) {
        int a;
        conn = DBConnector.getConnection();
        String checkSQL = "SELECT password FROM user WHERE userId = ?";
        try {
            pt = conn.prepareStatement(checkSQL);
            pt.setString(1, inpId);
            rs = pt.executeQuery();
            if (rs.next()) {
                if (rs.getString(1).equals(inpPassword)) {
                    a = 1;   // 로그인 성공
                } else {
                    a = 0;   // 비밀번호 불일치
                }
            } else {
                a = -1;   // 아이디가 존재하지 않음
            }

            rs.close();
            pt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            a = -2;  // DB 오류
        }
        return a;
    }

    public void logIn(String inpId) {
        conn = DBConnector.getConnection();
        String selectSQL = "SELECT * FROM user WHERE userId = ?";
        try {
            pt = conn.prepareStatement(selectSQL);
            pt.setString(1, inpId);
            rs = pt.executeQuery();
            if (rs.next()) {
                UserDTO userDTO = new UserDTO();

                userDTO.setNickName(rs.getString("nickName"));
                userDTO.setName(rs.getString("name"));
                userDTO.setUserId(rs.getString("userId"));
                userDTO.setPassword(rs.getString("password"));
                userDTO.setRegion(rs.getString("region"));
                userDTO.setPhoneNum(rs.getString("phoneNum"));
                userDTO.setBirth(rs.getString("birth"));

                new MainPage(userDTO);
            }

            rs.close();
            pt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("로그인 실패");
            e.printStackTrace();
        }
    }

    public void signUp(UserDTO userDTO) {
        conn = DBConnector.getConnection();
        String signInSQL = "INSERT INTO user (nickName, name, userId, password, region, phoneNum, birth) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            pt = conn.prepareStatement(signInSQL);

            pt.setString(1, userDTO.getNickName());
            pt.setString(2, userDTO.getName());
            pt.setString(3, userDTO.getUserId());
            pt.setString(4, userDTO.getPassword());
            pt.setString(5, userDTO.getRegion());
            pt.setString(6, userDTO.getPhoneNum());
            pt.setString(7, userDTO.getBirth());

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

    public void modifyUserInfo() {

    }

    public boolean nickNameCheck(String inp_nickName) {
        boolean nickNameCheck = false;
        conn = DBConnector.getConnection();
        String existssql = "SELECT exists(select nickName from user where nickName = ?) as cnt;";
        try {
            pt = conn.prepareStatement(existssql);
            pt.setString(1, inp_nickName);
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
