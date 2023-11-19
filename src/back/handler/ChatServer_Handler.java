package back.handler;

import back.request.Message_ChatRoom_Request;
import back.response.Message_ChatRoom_Response;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer_Handler extends Thread {
	private OutputStream os = null;
	private ObjectOutputStream oos = null;

	private InputStream is = null;
	private ObjectInputStream ois = null;

	private Socket socket = null;

	private ArrayList<ChatServer_Handler> list = null;

	private String master = null; //방장의 uuid를 저장하는 함수

	public ChatServer_Handler(Socket socket, ArrayList<ChatServer_Handler> list) {
		this.socket = socket;
		this.list = list;

		try {
			//서버 -> 클라이언트 Output Stream
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);

			//서버 <- 클라이언트 Input Stream
			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			Message_ChatRoom_Request join_Info = (Message_ChatRoom_Request) ois.readObject();

			sendAll(new Message_ChatRoom_Response(join_Info.nickName(), "이(가) 입장했습니다."));

			if (list.size() == 1) {
				master = join_Info.uuid();
			}

			while (true) {
				Object readObj = ois.readObject();

				if (readObj instanceof Message_ChatRoom_Request) {
					Message_ChatRoom_Request messageChatRoomRequest = (Message_ChatRoom_Request) readObj;

					if (master.equals(messageChatRoomRequest.uuid())) {
						Message_ChatRoom_Response messageChatRoomResponse = new Message_ChatRoom_Response(
								messageChatRoomRequest.nickName() + "(방장)",
								messageChatRoomRequest.message()
						);

						sendAll(messageChatRoomResponse);
					} else {
						Message_ChatRoom_Response messageChatRoomResponse = new Message_ChatRoom_Response(
								messageChatRoomRequest.nickName(),
								messageChatRoomRequest.message()
						);

						sendAll(messageChatRoomResponse);
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	private void sendAll(Message_ChatRoom_Response messageChatRoomResponse) {
		try {
			for (ChatServer_Handler handler : list) {
				handler.oos.writeObject(messageChatRoomResponse);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}