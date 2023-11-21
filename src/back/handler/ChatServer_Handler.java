package back.handler;

import back.ResponseCode;
import back.request.chatroom.Kick_ChatRoom_Request;
import back.request.chatroom.Message_ChatRoom_Request;
import back.response.chatroom.Message_ChatRoom_Response;

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

	private String nickName = null; //해당 사용자의 nickName

	private Boolean master = false; //방장 권한

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

			nickName = join_Info.nickName();

			sendAll(new Message_ChatRoom_Response(nickName, "이(가) 입장했습니다."));

			if (list.size() == 1) { //처음으로 접속한 사람 즉 글쓴이에게 방장 권한 부여
				master = true;
			}

			while (true) {
				Object readObj = ois.readObject();

				if (readObj instanceof Message_ChatRoom_Request) { //클라이언트 -> 서버 메세지 요청
					Message_ChatRoom_Request messageChatRoomRequest = (Message_ChatRoom_Request) readObj;

					if (master) {
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
				} else if (readObj instanceof Kick_ChatRoom_Request) {
					Kick_ChatRoom_Request kickChatRoomRequest = (Kick_ChatRoom_Request) readObj;

					if (master) {
						for (ChatServer_Handler handler : list) {
							if (kickChatRoomRequest.target_nickName().equals(handler.getnickName())) {
								sendAll(new Message_ChatRoom_Response("", kickChatRoomRequest.target_nickName() + "이(가) 강제퇴장 당했습니다."));

								handler.oos.close();
								handler.ois.close();
								handler.os.close();
								handler.is.close();
								handler.socket.close();

								list.remove(handler);
								break;
							}
						}

						oos.writeObject(ResponseCode.KICK_CHATROOM_SUCCESS);
					} else {
						oos.writeObject(ResponseCode.KICK_CHATROOM_FAILURE);
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

	private String getnickName() {
		return getnickName();
	}
}