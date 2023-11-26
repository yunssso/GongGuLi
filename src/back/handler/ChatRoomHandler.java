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

    /*채팅방 입장을 수행하는 메소드*/
    private void joinChatRoomMethod(JoinChatRoomRequest joinChatRoomRequest) {
        try {
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

    /*사용자가 들어있는 채팅방 목록*/
    private void getChattingRoomMethod(GetChattingRoomRequest getChattingRoomRequest) {
        try {
            GetChattingRoomDAO getChattingRoomDAO = new GetChattingRoomDAO();

            List<GetChattingRoomResponse> chattingRoomList = getChattingRoomDAO.getChattingRoomList(getChattingRoomRequest.uuid());

            if (chattingRoomList == null) { // 채팅방 목록 갱신 실패
                objectOutputStream.writeObject(ResponseCode.GET_CHATROOM_FAILURE);
            } else { // 채팅방 목록 갱신 성공
                objectOutputStream.writeObject(ResponseCode.GET_CHATROOM_SUCCESS);
                objectOutputStream.writeObject(chattingRoomList);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
