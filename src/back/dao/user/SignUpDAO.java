package back.dao.user;

import back.request.account.SignUpRequest;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

public class SignUpDAO {
    Connection conn = null;
    PreparedStatement pt = null;

    public void signUp(SignUpRequest signUpInfo) {
        String uuid = UUID.randomUUID().toString();
        String signInSQL = "INSERT INTO user (nickName, name, userId, password, region, phoneNum, birth, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = DBConnector.getConnection();
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
}
