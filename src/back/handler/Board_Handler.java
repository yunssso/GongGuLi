package back.handler;

import back.ResponseCode;
import back.dao.ChatRoomDAO;
import back.dao.PostingDAO;
import back.dao.GetInfoDAO;

import back.request.board.Delete_Board_Request;
import back.request.board.Edit_Board_Request;
import back.request.board.Post_Board_Request;
import back.request.chatroom.Join_ChatRoom_Request;
import back.response.chatroom.Join_ChatRoom_Response;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Board_Handler extends Thread {
    private Socket clientSocket = null;
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;

    public Board_Handler(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;

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

            if (readObj instanceof Post_Board_Request postBoardRequest) {
                postBoardMethod(postBoardRequest);
            } else if (readObj instanceof Edit_Board_Request editBoardRequest) {
                editBoardMethod(editBoardRequest);
            } else if (readObj instanceof Delete_Board_Request deleteBoardRequest) {
                deleteBoardMethod(deleteBoardRequest);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /*게시글 생성 함수*/
    private void postBoardMethod(Post_Board_Request postBoardRequest) {
        try {
            PostingDAO postingDAO = new PostingDAO();
            GetInfoDAO getInfoDAO = new GetInfoDAO();
            ChatRoomDAO chatRoomDAO = new ChatRoomDAO();

            //각 조건들을 비교하여 클라이언트에 응답을 보낸다.
            if (postBoardRequest.title().isBlank()) {
                objectOutputStream.writeObject(ResponseCode.TITLE_MISSING);
            } else if (postBoardRequest.region().equals(" --")) {
                objectOutputStream.writeObject(ResponseCode.REGION_NOT_SELECTED);
            } else if (postBoardRequest.category().equals(" --")) {
                objectOutputStream.writeObject(ResponseCode.CATEGORY_NOT_SELECTED);
            } else if (postBoardRequest.peopleNum().isBlank()) {
                objectOutputStream.writeObject(ResponseCode.PEOPLE_NUM_MISSING);
            } else if (postBoardRequest.content().isBlank()) {
                objectOutputStream.writeObject(ResponseCode.CONTENT_MISSING);
            } else {
                if (Integer.parseInt(postBoardRequest.peopleNum()) > 30) {
                    objectOutputStream.writeObject(ResponseCode.PEOPLE_NUM_OVER_LIMIT);
                } else if (Integer.parseInt(postBoardRequest.peopleNum()) <= 1) {
                    objectOutputStream.writeObject(ResponseCode.PEOPLE_NUM_UNDER_LIMIT);
                } else {
                    int port = chatRoomDAO.assignChatRoomPort(); // 랜덤한 채팅방 포트를 할당한다.
                    postingDAO.posting(postBoardRequest, port); // 게시글 생성
                    objectOutputStream.writeObject(ResponseCode.POST_BOARD_SUCCESS); // 게시글 생성 성공 응답을 보낸다.

                    Object readObj = objectInputStream.readObject(); // 채팅방 입장 요청을 받는다.

                    if (readObj instanceof Join_ChatRoom_Request joinChatRoomRequest) { // 채팅방 입장 요청일 경우
                        String nickName = getInfoDAO.getnickNameMethod(joinChatRoomRequest.uuid()); // uuid에 일치하는 닉네임을 가져옴

                        if (nickName != null) {
                            objectOutputStream.writeObject(ResponseCode.JOIN_CHATROOM_SUCCESS);
                            objectOutputStream.writeObject(new Join_ChatRoom_Response(nickName, port)); // 닉네임, 채팅방 port 정보를 보낸다.
                        } else {
                            objectOutputStream.writeObject(ResponseCode.JOIN_CHATROOM_FAILURE);
                        }
                    } else { // 그외의 요청이 들어올 경우
                        objectOutputStream.writeObject(ResponseCode.JOIN_CHATROOM_FAILURE);
                    }
                }
            }
        } catch (NumberFormatException numberFormatException) {
            try {
                objectOutputStream.writeObject(ResponseCode.PEOPLE_NUM_NOT_NUMBER);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /*게시글 수정 함수*/
    private void editBoardMethod(Edit_Board_Request editBoardRequest) {

    }

    /*게시글 삭제 함수*/
    private void deleteBoardMethod(Delete_Board_Request deleteBoardRequest) {

    }
}
