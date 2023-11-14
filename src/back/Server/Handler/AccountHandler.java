package back.Server.Handler;

import java.net.Socket;

import back.Server.Dto.FindUserIdDto;
import back.Server.Dto.SignUpDto;

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
			SignUpDto SignUpInfo = (SignUpDto) readObj;

			//각 조건들을 비교하여 클라이언트에 응답을 보낸다.
			String passwordStr = String.valueOf(SignUpInfo.password());
			if (SignUpInfo.userId().isBlank()) {
				oos.writeObject("400 BAD_REQUEST 아이디를 입력하세요.");
			} else if (passwordStr.isBlank()) {
				oos.writeObject("400 BAD_REQUEST 비밀번호를 입력하세요.");
			} else if (!passwordStr.equals(String.valueOf(SignUpInfo.passwordCheck()))) {
				oos.writeObject("400 BAD_REQUEST 비밀번호가 일치하지 않습니다.");
			} else if (passwordStr.length() < 8 ||
			!passwordStr.matches(".*[a-zA-Z].*") ||
			!passwordStr.matches(".*\\d.*") ||
			!passwordStr.matches(".*[@#$%^&*+_=!].*")) {
				oos.writeObject("400 BAD_REQUEST 비밀번호가 조건을 만족하지 않습니다.");
			} else if (SignUpInfo.name().isBlank()) {
				oos.writeObject("400 BAD_REQUEST 이름을 입력하세요.");
			} else if (SignUpInfo.birth().isBlank()) {
				oos.writeObject("400 BAD_REQUEST 생년월일을 입력하세요.");
			} else if (!SignUpInfo.birth().matches("\\d{6}")) {
				oos.writeObject("400 BAD_REQUEST 생년월일이 조건을 만족하지 않습니다.");
			} else if (SignUpInfo.phoneNumber().isBlank()) {
				oos.writeObject("400 BAD_REQUES 전화번호를 입력하세요.");
			} else if (!SignUpInfo.phoneNumber().matches("\\d{11}")) {
				oos.writeObject("400 BAD_REQUEST 전화번호가 조건을 만족하지 않습니다.");
			} else if (SignUpInfo.nickName().isBlank()) {
				oos.writeObject("400 BAD_REQUEST 닉네임을 입력하세요.");
			} else if (SignUpInfo.region().equals("거주 지역")) {
				oos.writeObject("400 BAD_REQUEST 거주 지역을 선택하세요.");
			} else {
				oos.writeObject("201 CREATED 회원가입이 완료되었습니다.");
			}
            
			oos.close();
			os.close();

			ois.close();
			is.close();

			clientSocket.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public void FindUserIdMethod(Object readObj) {
		//클라이언트에서 회원가입 요청을 받는다.
		FindUserIdDto FindUserIdInfo = (FindUserIdDto) readObj;

		
		


	}
}