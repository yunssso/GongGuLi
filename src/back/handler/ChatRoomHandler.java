package back.handler;

import back.ResponseCode;
import back.dao.GetInfoDAO;
import back.dao.chatting.GetChattingRoomDAO;
import back.request.chatroom.GetChattingRoomRequest;
import back.request.chatroom.JoinChatRoomRequest;
import back.response.chatroom.GetChattingRoomResponse;
import back.response.chatroom.JoinChatRoomResponse;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class ChatRoomHandler extends Thread {
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;

    public ChatRoomHandler(Socket clientSocket) {
        try {

            InputStream inputStream = clientSocket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);

            OutputStream outputStream = clientSocket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Object readObj = objectInputStream.readObject();

            if (readObj instanceof  JoinChatRoomRequest joinChatRoomRequest) {
                joinChatRoomMethod(joinChatRoomRequest);
            } else if (readObj instanceof GetChattingRoomRequest getChattingRoomRequest) {
                getChattingRoomMethod(getChattingRoomRequest);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void joinChatRoomMethod(JoinChatRoomRequest joinChatRoomRequest) {
        try {
            //사용자에게 접속을 원하는 게시글 id, uuid 정보를 받아와서 처리할 거 처리하고 포트 정보 및 대화 내용 등 return 해주는 DAO 필요
            GetInfoDAO getInfoDAO = new GetInfoDAO();

            String nickName = getInfoDAO.getnickNameMethod(joinChatRoomRequest.uuid());
            int chatPort = getInfoDAO.getchatPortMethod(joinChatRoomRequest.selectRow());

            if (chatPort != -1) {
                objectOutputStream.writeObject(ResponseCode.JOIN_CHATROOM_SUCCESS);
                objectOutputStream.writeObject(new JoinChatRoomResponse(nickName, chatPort));
            } else {
                objectOutputStream.writeObject(ResponseCode.JOIN_CHATROOM_FAILURE);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

//    사용자가 들어있는 채팅방 목록
    private void getChattingRoomMethod(GetChattingRoomRequest getChattingRoomRequest) {
        try {
            GetChattingRoomDAO getChattingRoomDAO = new GetChattingRoomDAO();

            String uuid = getChattingRoomRequest.uuid();
            List<GetChattingRoomResponse> chattingRoomList = getChattingRoomDAO.getChattingRoomList(uuid);

            if (chattingRoomList == null) {
                objectOutputStream.writeObject(ResponseCode.JOIN_CHATROOM_FAILURE);      // GetChattingRoom_Fail, Success ResponseCode 만들어주세요.
            } else {
                objectOutputStream.writeObject(chattingRoomList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
