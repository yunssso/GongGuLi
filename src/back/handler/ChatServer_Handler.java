package back.handler;

import back.ResponseCode;
import back.request.chatroom.Kick_ChatRoom_Request;
import back.request.chatroom.Message_ChatRoom_Request;
import back.response.chatroom.Message_ChatRoom_Response;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer_Handler extends Thread {
	private ObjectOutputStream objectOutputStream = null;

	private ObjectInputStream objectInputStream = null;

	private ArrayList<ChatServer_Handler> list = null;

	private Boolean master = false; //방장 권한

	public ChatServer_Handler(Socket socket, ArrayList<ChatServer_Handler> list) {
		try {
			this.list = list;

			//서버 -> 클라이언트 Output Stream
			OutputStream outputStream = socket.getOutputStream();
			objectOutputStream = new ObjectOutputStream(outputStream);

			//서버 <- 클라이언트 Input Stream
			InputStream inputStream = socket.getInputStream();
			objectInputStream = new ObjectInputStream(inputStream);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			Message_ChatRoom_Request join_Info = (Message_ChatRoom_Request) objectInputStream.readObject();

			//해당 사용자의 nickName
			String nickName = join_Info.nickName();

			sendAll(new Message_ChatRoom_Response(nickName, "이(가) 입장했습니다."));

			if (list.size() == 1) { //처음으로 접속한 사람 즉 글쓴이에게 방장 권한 부여
				master = true;
			}

			while (true) {
				Object readObj = objectInputStream.readObject();

				if (readObj instanceof Message_ChatRoom_Request messageChatRoomRequest) { //클라이언트 -> 서버 메세지 요청
					if (master) { // 방장이 메세지를 보냈을 때
						Message_ChatRoom_Response messageChatRoomResponse = new Message_ChatRoom_Response(
								messageChatRoomRequest.nickName() + "(방장)",
								messageChatRoomRequest.message()
						);

						sendAll(messageChatRoomResponse);
					} else { // 이외 사용자가 메세지를 보냈을 때
						Message_ChatRoom_Response messageChatRoomResponse = new Message_ChatRoom_Response(
								messageChatRoomRequest.nickName(),
								messageChatRoomRequest.message()
						);

						sendAll(messageChatRoomResponse);
					}
				} else if (readObj instanceof Kick_ChatRoom_Request kickChatRoomRequest) { // 클라이언트 -> 서버 강퇴 요청
					if (master) { // 방장일 경우에만 강퇴 요청을 accept
						for (ChatServer_Handler handler : list) {
							if (kickChatRoomRequest.target_nickName().equals(handler.getnickName())) {
								sendAll(new Message_ChatRoom_Response("", kickChatRoomRequest.target_nickName() + "이(가) 강제퇴장 당했습니다."));

								handler.objectInputStream.close();
								list.remove(handler);
								break;
							}
						}

						objectOutputStream.writeObject(ResponseCode.KICK_CHATROOM_SUCCESS);
					} else {
						objectOutputStream.writeObject(ResponseCode.KICK_CHATROOM_FAILURE);
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
				handler.objectOutputStream.writeObject(messageChatRoomResponse);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private String getnickName() {
		return getnickName();
	}
}