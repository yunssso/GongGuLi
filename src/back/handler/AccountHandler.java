package back.handler;

import java.net.Socket;

import back.ResponseCode;
import back.dao.UserDAO;
import back.dto.*;
import back.response.FindUserIdResponse;
import back.response.FindUserPasswordResponse;
import back.response.LoginResponse;

import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.UUID;

public class AccountHandler extends Thread {
	private Socket clientSocket = null;

	private OutputStream os = null;
	private ObjectOutputStream oos = null;

	private InputStream is = null;
	private ObjectInputStream ois = null;

	private final UserDAO userDAO = new UserDAO();

	public AccountHandler(Socket clientSocket) {
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

			if (readObj instanceof SignUpDto) {
				SignUpMethod(readObj);
			} else if (readObj instanceof LoginDto) {
				LoginMethod(readObj);
			} else if (readObj instanceof FindUserIdDto) {
				FindUserIdMethod(readObj);
			} else if (readObj instanceof  FindUserPasswordDto) {
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
			SignUpDto SignUpInfo = (SignUpDto) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			if (SignUpInfo.userId().isBlank()) {
				oos.writeObject(ResponseCode.ID_MISSING);
			} else if (SignUpInfo.password().isBlank()) {
				oos.writeObject(ResponseCode.PASSWORD_MISSING);
			} else if (!SignUpInfo.password().equals(String.valueOf(SignUpInfo.passwordCheck()))) {
				oos.writeObject(ResponseCode.PASSWORD_MISMATCH);
			} else if (SignUpInfo.password().length() < 8 ||
					!SignUpInfo.password().matches(".*[a-zA-Z].*") ||
					!SignUpInfo.password().matches(".*\\d.*") ||
					!SignUpInfo.password().matches(".*[@#$%^&*+_=!].*")) {
				oos.writeObject(ResponseCode.PASSWORD_CONDITIONS_NOT_MET);
			} else if (SignUpInfo.name().isBlank()) {
				oos.writeObject(ResponseCode.NAME_MISSING);
			} else if (SignUpInfo.birth().isBlank()) {
				oos.writeObject(ResponseCode.BIRTHDAY_MISSING);
			} else if (!SignUpInfo.birth().matches("\\d{6}")) {
				oos.writeObject(ResponseCode.BIRTHDAY_CONDITIONS_NOT_MET);
			} else if (SignUpInfo.phoneNumber().isBlank()) {
				oos.writeObject(ResponseCode.PHONE_NUMBER_MISSING);
			} else if (!SignUpInfo.phoneNumber().matches("\\d{11}")) {
				oos.writeObject(ResponseCode.PHONE_NUMBER_CONDITIONS_NOT_MET);
			} else if (SignUpInfo.nickName().isBlank()) {
				oos.writeObject(ResponseCode.NICKNAME_MISSING);
			} else if (SignUpInfo.region().equals("거주 지역")) {
				oos.writeObject(ResponseCode.RESIDENCE_AREA_NOT_SELECTED);
			} else {
				String uuid = UUID.randomUUID().toString();
				userDAO.signUp(SignUpInfo, uuid);
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
			LoginDto LoginInfo = (LoginDto) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			if (LoginInfo.userId().isBlank()) {
				oos.writeObject(ResponseCode.ID_MISSING);
			} else if (LoginInfo.password().isBlank()) {
				oos.writeObject(ResponseCode.PASSWORD_MISSING);
			} else {
				String logInCheckResult = userDAO.logInCheck(LoginInfo);
				if (logInCheckResult.equals("0")) {
					oos.writeObject(ResponseCode.PASSWORD_MISMATCH_LOGIN);
				} else if (logInCheckResult.equals("-1")) {
					oos.writeObject(ResponseCode.ID_NOT_EXIST);
				} else if (!logInCheckResult.equals("-2")) {
					oos.writeObject(ResponseCode.LOGIN_SUCCESS);
					oos.writeObject(new LoginResponse(logInCheckResult));
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
			FindUserIdDto findUserIdDto = (FindUserIdDto) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			if (findUserIdDto.name().isBlank()) {
				oos.writeObject(ResponseCode.NAME_MISSING);
			} else if (findUserIdDto.birth().isBlank()) {
				oos.writeObject(ResponseCode.BIRTHDAY_MISSING);
			} else if (findUserIdDto.phoneNumber().isBlank()) {
				oos.writeObject(ResponseCode.PHONE_NUMBER_MISSING);
			} else {
				FindUserIdResponse findUserIdResponse = new FindUserIdResponse(userDAO.findID(findUserIdDto));

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
			FindUserPasswordDto findUserPasswordDto = (FindUserPasswordDto) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			if (findUserPasswordDto.name().isBlank()) {
				oos.writeObject(ResponseCode.NAME_MISSING);
			} else if (findUserPasswordDto.userId().isBlank()) {
				oos.writeObject(ResponseCode.ID_MISSING);
			} else if (findUserPasswordDto.birth().isBlank()) {
				oos.writeObject(ResponseCode.BIRTHDAY_MISSING);
			} else if (findUserPasswordDto.phoneNumber().isBlank()) {
				oos.writeObject(ResponseCode.PHONE_NUMBER_MISSING);
			} else {
				FindUserPasswordResponse findUserPasswordResponse = new FindUserPasswordResponse(userDAO.findPassword(findUserPasswordDto));

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