package back.server.handler;

import java.net.Socket;

import back.dto.FindUserIdDto;
import back.dto.ResponseDto;
import back.dto.SignUpDto;
import back.dao.UserDAO;

import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class AccountHandler extends Thread {
	UserDAO userDAO = new UserDAO();
	private Socket clientSocket = null;

	private OutputStream os = null;
	private ObjectOutputStream oos = null;

	private InputStream is = null;
	private ObjectInputStream ois = null;

	public AccountHandler(Socket clientSocket) throws Exception {
		this.clientSocket = clientSocket;

		//서버 -> 클라이언트 Output Stream
		os = clientSocket.getOutputStream();
		oos = new ObjectOutputStream(os);

		//서버 <- 클라이언트 Input Stream
		is = clientSocket.getInputStream();
		ois = new ObjectInputStream(is);
	}
	
	@Override
	public void run() {
		try {
			Object readObj = ois.readObject();

			if (readObj instanceof SignUpDto) {
				SignUpMethod(readObj);
			} else if (readObj instanceof FindUserIdDto) {
				FindUserIdMethod(readObj);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void SignUpMethod(Object readObj) {
		try {
			//클라이언트에서 회원가입 요청을 받는다.
			SignUpDto signUpInfo = (SignUpDto) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			String passwordStr = String.valueOf(signUpInfo.password());
			if (signUpInfo.userId().isBlank()) {
				oos.writeObject(new ResponseDto(201, "아이디를 입력하세요."));
			} else if (passwordStr.isBlank()) {
				oos.writeObject(new ResponseDto(202, "비밀번호를 입력하세요."));
			} else if (!passwordStr.equals(String.valueOf(signUpInfo.passwordCheck()))) {
				oos.writeObject(new ResponseDto(203, "비밀번호가 일치하지 않습니다."));
			} else if (passwordStr.length() < 8 ||
					!passwordStr.matches(".*[a-zA-Z].*") ||
					!passwordStr.matches(".*\\d.*") ||
					!passwordStr.matches(".*[@#$%^&*+_=!].*")) {
				oos.writeObject(new ResponseDto(204, "비밀번호가 조건을 만족하지 않습니다."));
			} else if (signUpInfo.name().isBlank()) {
				oos.writeObject(new ResponseDto(205, "이름을 입력하세요."));
			} else if (signUpInfo.birth().isBlank()) {
				oos.writeObject(new ResponseDto(206, "생년월일을 입력하세요."));
			} else if (!signUpInfo.birth().matches("\\d{6}")) {
				oos.writeObject(new ResponseDto(207, "생년월일이 조건을 만족하지 않습니다."));
			} else if (signUpInfo.phoneNumber().isBlank()) {
				oos.writeObject(new ResponseDto(208, "전화번호를 입력하세요."));
			} else if (!signUpInfo.phoneNumber().matches("\\d{11}")) {
				oos.writeObject(new ResponseDto(209, "전화번호가 조건을 만족하지 않습니다."));
			} else if (signUpInfo.nickName().isBlank()) {
				oos.writeObject(new ResponseDto(210, "닉네임을 입력하세요."));
			} else if (signUpInfo.region().equals("거주 지역")) {
				oos.writeObject(new ResponseDto(211, "거주 지역을 선택하세요."));
			} else {
				//! 회원가입 데이터베이스 연동해줘
				userDAO.signUp(signUpInfo);
				oos.writeObject(new ResponseDto(200, "회원가입이 완료되었습니다."));
			}
            
			CloseHandler();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void FindUserIdMethod(Object readObj) {
		try {
			// 클라이언트에서 회원가입 요청을 받는다.
			FindUserIdDto FindUserIdInfo = (FindUserIdDto) readObj;
			
			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			if (FindUserIdInfo.name().isBlank()) {
				oos.writeObject(new ResponseDto(205, "이름을 입력하세요."));
			} else if (FindUserIdInfo.birth().isBlank()) {
				oos.writeObject(new ResponseDto(206, "생년월일을 입력하세요."));
			} else if (FindUserIdInfo.phoneNumber().isBlank()) {
				oos.writeObject(new ResponseDto(208, "전화번호를 입력하세요."));
			} else {
				//! 아이디 찾기 데이터베이스 연동해줘
				oos.writeObject(new ResponseDto(220, "아이디 찾기가 완료되었습니다."));
			}

			CloseHandler();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	//작업을 마치고 스트림과 소켓을 닫는 함수
	public void CloseHandler() {
		try {
			oos.close();
			os.close();

			ois.close();
			is.close();

			clientSocket.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				oos.close();
				os.close();

				ois.close();
				is.close();

				clientSocket.close();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}
}