package back.handler;

import back.dao.chatting.LeaveChatRoomDAO;
import back.request.chatroom.JoinChatRoomListRequest;
import back.request.chatroom.LeaveChatRoomRequest;
import back.response.ResponseCode;
import back.dao.chatting.GetChatRoomListDAO;
import back.dao.chatting.JoinChattingRoomDAO;
import back.request.chatroom.GetChattingRoomRequest;
import back.request.chatroom.JoinChatRoomRequest;
import back.response.chatroom.GetChattingRoomResponse;
import back.response.chatroom.JoinChatRoomListResponse;

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
            } else if (readObj instanceof LeaveChatRoomRequest leaveChatRoomRequest) {
                leaveChatRoomMethod(leaveChatRoomRequest);
            } else if (readObj instanceof JoinChatRoomListRequest joinChatRoomListRequest) {
                joinChatRoomListMethod(joinChatRoomListRequest);
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

    /*채팅방 나가기 Response를 보내는 메소드*/
    private void leaveChatRoomMethod(LeaveChatRoomRequest leaveChatRoomRequest) {
        try {
            // 여기에 넘어온 port와 uuid 이용해서 DB에서 제거 해주면 돼 <= 여기에 DAO 이용해서 제거 해줘
            LeaveChatRoomDAO leaveChatRoomDAO = new LeaveChatRoomDAO();
            boolean res = leaveChatRoomDAO.leaveChatRoom(leaveChatRoomRequest.port(), leaveChatRoomRequest.uuid());
            if (res) {
                objectOutputStream.writeObject(ResponseCode.LEAVE_CHATROOM_SUCCESS);
            } else {
                objectOutputStream.writeObject(ResponseCode.LEAVE_CHATROOM_FAILURE);
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

    /*채팅방 참여 리스트 Response를 보내는 메소드*/
    private void joinChatRoomListMethod(JoinChatRoomListRequest joinChatRoomListRequest) {
        try {
            GetChatRoomListDAO getChatRoomListDAO = new GetChatRoomListDAO();

            int port = getChatRoomListDAO.getInChattingroom(joinChatRoomListRequest.selectRow(), joinChatRoomListRequest.uuid());

            JoinChatRoomListResponse joinChatRoomListResponse = new JoinChatRoomListResponse(port);

            if (port != -1) {
                objectOutputStream.writeObject(ResponseCode.JOIN_CHATROOM_SUCCESS);
                objectOutputStream.writeObject(joinChatRoomListResponse);
            } else {
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
}
