package back.dao.user;

import back.response.ResponseCode;
import back.request.account.SignUpRequest;
import database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;

public class SignUpDAO {
    Connection conn = null;
    PreparedStatement pt = null;

    public ResponseCode signUp(SignUpRequest signUpInfo) {
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
                return ResponseCode.SIGNUP_SUCCESS;   //  회원가입 성공
            }

            pt.close();
            conn.close();
        } catch (SQLIntegrityConstraintViolationException e) {
            int errorCode = e.getErrorCode();
            if (errorCode == 40) {
                return ResponseCode.ID_DUPLICATE;   //  아이디 중복
            } else if (errorCode == 41) {
                return ResponseCode.PHONE_NUMBER_DUPLICATE;   //  핸드폰 번호 중복
            } else if (errorCode == 490) {
                return ResponseCode.NICKNAME_DUPLICATE;     //  닉네임 중복
            } else {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseCode.SIGNUP_FAILURE;
    }
}
