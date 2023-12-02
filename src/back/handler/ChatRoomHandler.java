package back.handler;

import back.response.ResponseCode;
import back.dao.chatting.GetChatRoomListDAO;
import back.dao.chatting.JoinChattingRoomDAO;
import back.request.chatroom.GetChattingRoomRequest;
import back.request.chatroom.JoinChatRoomRequest;
import back.response.chatroom.GetChattingRoomResponse;

import java.io.*;
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

            try {
                objectInputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /*사용자의 Request를 받는 메소드*/
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

            try {
                objectInputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /*채팅방 입장 Response를 보내는 메소드*/
    private void joinChatRoomMethod(JoinChatRoomRequest joinChatRoomRequest) {
        try {
            JoinChattingRoomDAO joinChattingRoomDAO = new JoinChattingRoomDAO();
            int result = joinChattingRoomDAO.joinChattingRoom(joinChatRoomRequest.port(), joinChatRoomRequest.uuid());
            if (result == 1) {
                objectOutputStream.writeObject(ResponseCode.JOIN_CHATROOM_SUCCESS);
            } else if (result == 0) {
                objectOutputStream.writeObject(ResponseCode.CHATROOM_FULL);
            } else if (result == -1) {
                objectOutputStream.writeObject(ResponseCode.JOIN_CHATROOM_FAILURE);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                objectInputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /*채팅방 목록 갱신 Response를 보내는 메소드*/
    private void getChattingRoomMethod(GetChattingRoomRequest getChattingRoomRequest) {
        try {
            GetChatRoomListDAO getChatRoomListDAO = new GetChatRoomListDAO();

            List<GetChattingRoomResponse> chattingRoomList = getChatRoomListDAO.getChattingRoomList(getChattingRoomRequest.uuid());

            if (chattingRoomList == null) { // 채팅방 목록 갱신 실패
                objectOutputStream.writeObject(ResponseCode.GET_CHATROOM_FAILURE);
            } else { // 채팅방 목록 갱신 성공
                objectOutputStream.writeObject(ResponseCode.GET_CHATROOM_SUCCESS);

                objectOutputStream.writeObject(chattingRoomList);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                objectInputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
