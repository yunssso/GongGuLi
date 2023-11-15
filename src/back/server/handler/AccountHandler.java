package back.server.handler;

import java.net.Socket;

import back.dao.UserDAO;
import back.dto.*;

import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

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
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void SignUpMethod(Object readObj) {
		try {
			//클라이언트에서 회원가입 요청을 받는다.
			SignUpDto SignUpInfo = (SignUpDto) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			if (SignUpInfo.userId().isBlank()) {
				oos.writeObject(new ResponseDto(201, "아이디를 입력하세요."));
			} else if (SignUpInfo.password().isBlank()) {
				oos.writeObject(new ResponseDto(202, "비밀번호를 입력하세요."));
			} else if (!SignUpInfo.password().equals(String.valueOf(SignUpInfo.passwordCheck()))) {
				oos.writeObject(new ResponseDto(203, "비밀번호가 일치하지 않습니다."));
			} else if (SignUpInfo.password().length() < 8 ||
					!SignUpInfo.password().matches(".*[a-zA-Z].*") ||
					!SignUpInfo.password().matches(".*\\d.*") ||
					!SignUpInfo.password().matches(".*[@#$%^&*+_=!].*")) {
				oos.writeObject(new ResponseDto(204, "비밀번호가 조건을 만족하지 않습니다."));
			} else if (SignUpInfo.name().isBlank()) {
				oos.writeObject(new ResponseDto(205, "이름을 입력하세요."));
			} else if (SignUpInfo.birth().isBlank()) {
				oos.writeObject(new ResponseDto(206, "생년월일을 입력하세요."));
			} else if (!SignUpInfo.birth().matches("\\d{6}")) {
				oos.writeObject(new ResponseDto(207, "생년월일이 조건을 만족하지 않습니다."));
			} else if (SignUpInfo.phoneNumber().isBlank()) {
				oos.writeObject(new ResponseDto(208, "전화번호를 입력하세요."));
			} else if (!SignUpInfo.phoneNumber().matches("\\d{11}")) {
				oos.writeObject(new ResponseDto(209, "전화번호가 조건을 만족하지 않습니다."));
			} else if (SignUpInfo.nickName().isBlank()) {
				oos.writeObject(new ResponseDto(210, "닉네임을 입력하세요."));
			} else if (SignUpInfo.region().equals("거주 지역")) {
				oos.writeObject(new ResponseDto(211, "거주 지역을 선택하세요."));
			} else {
				userDAO.signUp(SignUpInfo);
				oos.writeObject(new ResponseDto(200, "회원가입이 완료되었습니다."));
			}

			CloseHandler();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void LoginMethod(Object readObj) {
		try {
			//클라이언트에서 로그인 요청을 받는다.
			LoginDto LoginInfo = (LoginDto) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			if (LoginInfo.userId().isBlank()) {
				oos.writeObject(new ResponseDto(201, "아이디를 입력하세요."));
			} else if (LoginInfo.password().isBlank()) {
				oos.writeObject(new ResponseDto(202, "비밀번호를 입력하세요."));
			} else if (LoginInfo.password().length() < 8 ||
					!LoginInfo.password().matches(".*[a-zA-Z].*") ||
					!LoginInfo.password().matches(".*\\d.*") ||
					!LoginInfo.password().matches(".*[@#$%^&*+_=!].*")) {
				oos.writeObject(new ResponseDto(204, "비밀번호가 조건을 만족하지 않습니다."));
			} else {
				int logInCheckResult = userDAO.logInCheck(LoginInfo);

				if (logInCheckResult == 1) {
					oos.writeObject(new ResponseDto(220, "로그인이 완료되었습니다."));
				} else if (logInCheckResult == 0) {
					oos.writeObject(new ResponseDto(221, "비밀번호가 일치하지 않습니다."));
				} else if (logInCheckResult == -1) {
					oos.writeObject(new ResponseDto(222, "존재하지 않은 아이디 입니다."));
				}
			}

			CloseHandler();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void FindUserIdMethod(Object readObj) {
		try {
			//클라이언트에서 아이디 찾기 요청을 받는다.
			FindUserIdDto FindUserIdInfo = (FindUserIdDto) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			if (FindUserIdInfo.name().isBlank()) {
				oos.writeObject(new ResponseDto(205, "이름을 입력하세요."));
			} else if (FindUserIdInfo.birth().isBlank()) {
				oos.writeObject(new ResponseDto(206, "생년월일을 입력하세요."));
			} else if (!FindUserIdInfo.birth().matches("\\d{6}")) {
				oos.writeObject(new ResponseDto(207, "생년월일이 조건을 만족하지 않습니다."));
			} else if (FindUserIdInfo.phoneNumber().isBlank()) {
				oos.writeObject(new ResponseDto(208, "전화번호를 입력하세요."));
			} else if (!FindUserIdInfo.phoneNumber().matches("\\d{11}")) {
				oos.writeObject(new ResponseDto(209, "전화번호가 조건을 만족하지 않습니다."));
			} else {
				if (userDAO.findID(FindUserIdInfo)) {
					oos.writeObject(new ResponseDto(230, "아이디 찾기가 완료되었습니다."));
				} else {
					oos.writeObject(new ResponseDto(231, "일치하는 회원이 없습니다."));
				}
			}

			CloseHandler();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void FindUserPasswordMethod(Object readObj) {
		try {
			//클라이언트에서 아이디 찾기 요청을 받는다.
			FindUserPasswordDto FindUserPasswordInfo = (FindUserPasswordDto) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			if (FindUserPasswordInfo.name().isBlank()) {
				oos.writeObject(new ResponseDto(205, "이름을 입력하세요."));
			} else if (FindUserPasswordInfo.userId().isBlank()) {
				oos.writeObject(new ResponseDto(201, "아이디를 입력하세요."));
			} else if (FindUserPasswordInfo.birth().isBlank()) {
				oos.writeObject(new ResponseDto(206, "생년월일을 입력하세요."));
			} else if (!FindUserPasswordInfo.birth().matches("\\d{6}")) {
				oos.writeObject(new ResponseDto(207, "생년월일이 조건을 만족하지 않습니다."));
			} else if (FindUserPasswordInfo.phoneNumber().isBlank()) {
				oos.writeObject(new ResponseDto(208, "전화번호를 입력하세요."));
			} else if (!FindUserPasswordInfo.phoneNumber().matches("\\d{11}")) {
				oos.writeObject(new ResponseDto(209, "전화번호가 조건을 만족하지 않습니다."));
			} else {
				if (userDAO.findPassword(FindUserPasswordInfo)) {
					oos.writeObject(new ResponseDto(240, "비밀번호 찾기가 완료되었습니다."));
				} else {
					oos.writeObject(new ResponseDto(241, "일치하는 회원이 없습니다."));
				}
			}

			CloseHandler();
		} catch (Exception exception) {
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