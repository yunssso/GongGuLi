package back.handler;

import java.net.Socket;

import back.ResponseCode;
import back.dao.UserDAO;
import back.request.*;
import back.response.Find_UserId_Response;
import back.response.Find_UserPassword_Response;
import back.response.Login_Response;

import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.UUID;

public class Account_Handler extends Thread {
	private Socket clientSocket = null;

	private OutputStream os = null;
	private ObjectOutputStream oos = null;

	private InputStream is = null;
	private ObjectInputStream ois = null;

	private final UserDAO userDAO = new UserDAO();

	public Account_Handler(Socket clientSocket) {
		try {
			this.clientSocket = clientSocket;

			//서버 -> 클라이언트 Output Stream
			os = clientSocket.getOutputStream();
			oos = new ObjectOutputStream(os);

			//서버 <- 클라이언트 Input Stream
			is = clientSocket.getInputStream();
			ois = new ObjectInputStream(is);
		} catch (Exception exception) {
			CloseHandler();
			exception.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			Object readObj = ois.readObject();

			if (readObj instanceof SignUp_Request) {
				SignUpMethod(readObj);
			} else if (readObj instanceof Login_Request) {
				LoginMethod(readObj);
			} else if (readObj instanceof Find_UserId_Request) {
				FindUserIdMethod(readObj);
			} else if (readObj instanceof Find_UserPassword_Request) {
				FindUserPasswordMethod(readObj);
			}
		} catch (Exception exception) {
			CloseHandler();
			exception.printStackTrace();
		}
	}

	private void SignUpMethod(Object readObj) {
		try {
			//클라이언트에서 회원가입 요청을 받는다.
			SignUp_Request signUpRequest = (SignUp_Request) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			if (signUpRequest.userId().isBlank()) {
				oos.writeObject(ResponseCode.ID_MISSING);
			} else if (signUpRequest.password().isBlank()) {
				oos.writeObject(ResponseCode.PASSWORD_MISSING);
			} else if (!signUpRequest.password().equals(String.valueOf(signUpRequest.passwordCheck()))) {
				oos.writeObject(ResponseCode.PASSWORD_MISMATCH);
			} else if (signUpRequest.password().length() < 8 ||
					!signUpRequest.password().matches(".*[a-zA-Z].*") ||
					!signUpRequest.password().matches(".*\\d.*") ||
					!signUpRequest.password().matches(".*[@#$%^&*+_=!].*")) {
				oos.writeObject(ResponseCode.PASSWORD_CONDITIONS_NOT_MET);
			} else if (signUpRequest.name().isBlank()) {
				oos.writeObject(ResponseCode.NAME_MISSING);
			} else if (signUpRequest.birth().isBlank()) {
				oos.writeObject(ResponseCode.BIRTHDAY_MISSING);
			} else if (!signUpRequest.birth().matches("\\d{6}")) {
				oos.writeObject(ResponseCode.BIRTHDAY_CONDITIONS_NOT_MET);
			} else if (signUpRequest.phoneNumber().isBlank()) {
				oos.writeObject(ResponseCode.PHONE_NUMBER_MISSING);
			} else if (!signUpRequest.phoneNumber().matches("\\d{11}")) {
				oos.writeObject(ResponseCode.PHONE_NUMBER_CONDITIONS_NOT_MET);
			} else if (signUpRequest.nickName().isBlank()) {
				oos.writeObject(ResponseCode.NICKNAME_MISSING);
			} else if (signUpRequest.region().equals("거주 지역")) {
				oos.writeObject(ResponseCode.RESIDENCE_AREA_NOT_SELECTED);
			} else {
				String uuid = UUID.randomUUID().toString();
				userDAO.signUp(signUpRequest, uuid);
				oos.writeObject(ResponseCode.SIGNUP_SUCCESS);
			}

			CloseHandler();
		} catch (Exception exception) {
			CloseHandler();
			exception.printStackTrace();
		}
	}

	private void LoginMethod(Object readObj) {
		try {
			//클라이언트에서 로그인 요청을 받는다.
			Login_Request loginRequest = (Login_Request) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			if (loginRequest.userId().isBlank()) {
				oos.writeObject(ResponseCode.ID_MISSING);
			} else if (loginRequest.password().isBlank()) {
				oos.writeObject(ResponseCode.PASSWORD_MISSING);
			} else {
				String logInCheckResult = userDAO.logInCheck(loginRequest);
				if (logInCheckResult.equals("0")) {
					oos.writeObject(ResponseCode.PASSWORD_MISMATCH_LOGIN);
				} else if (logInCheckResult.equals("-1")) {
					oos.writeObject(ResponseCode.ID_NOT_EXIST);
				} else if (!logInCheckResult.equals("-2")) {
					oos.writeObject(ResponseCode.LOGIN_SUCCESS);
					oos.writeObject(new Login_Response(logInCheckResult));
				}
			}
			CloseHandler();
		} catch (Exception exception) {
			CloseHandler();
			exception.printStackTrace();
		}
	}

	private void FindUserIdMethod(Object readObj) {
		try {
			//클라이언트에서 아이디 찾기 요청을 받는다.
			Find_UserId_Request findUserIdRequest = (Find_UserId_Request) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			if (findUserIdRequest.name().isBlank()) {
				oos.writeObject(ResponseCode.NAME_MISSING);
			} else if (findUserIdRequest.birth().isBlank()) {
				oos.writeObject(ResponseCode.BIRTHDAY_MISSING);
			} else if (findUserIdRequest.phoneNumber().isBlank()) {
				oos.writeObject(ResponseCode.PHONE_NUMBER_MISSING);
			} else {
				Find_UserId_Response findUserIdResponse = new Find_UserId_Response(userDAO.findID(findUserIdRequest));

				if (!findUserIdResponse.userId().isEmpty()) {
					oos.writeObject(ResponseCode.FIND_ID_SUCCESS);
					oos.writeObject(findUserIdResponse);
				} else {
					oos.writeObject(ResponseCode.NO_MATCHING_USER);
				}
			}

			CloseHandler();
		} catch (Exception exception) {
			CloseHandler();
			exception.printStackTrace();
		}
	}

	private void FindUserPasswordMethod(Object readObj) {
		try {
			//클라이언트에서 아이디 찾기 요청을 받는다.
			Find_UserPassword_Request findUserPasswordRequest = (Find_UserPassword_Request) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			if (findUserPasswordRequest.name().isBlank()) {
				oos.writeObject(ResponseCode.NAME_MISSING);
			} else if (findUserPasswordRequest.userId().isBlank()) {
				oos.writeObject(ResponseCode.ID_MISSING);
			} else if (findUserPasswordRequest.birth().isBlank()) {
				oos.writeObject(ResponseCode.BIRTHDAY_MISSING);
			} else if (findUserPasswordRequest.phoneNumber().isBlank()) {
				oos.writeObject(ResponseCode.PHONE_NUMBER_MISSING);
			} else {
				Find_UserPassword_Response findUserPasswordResponse = new Find_UserPassword_Response(userDAO.findPassword(findUserPasswordRequest));

				if (!findUserPasswordResponse.password().isEmpty()) {
					oos.writeObject(ResponseCode.FIND_PASSWORD_SUCCESS);
					oos.writeObject(findUserPasswordResponse);
				} else {
					oos.writeObject(ResponseCode.NO_MATCHING_USER);
				}
			}

			CloseHandler();
		} catch (Exception exception) {
			CloseHandler();
			exception.printStackTrace();
		}
	}

	private void CloseHandler() {
		try {
			oos.close();
			os.close();

			ois.close();
			is.close();

			clientSocket.close();
		} catch(Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				oos.close();
				os.close();

				ois.close();
				is.close();

				clientSocket.close();
			} catch(Exception exception) {
				exception.printStackTrace();
			}
		}
	}
}