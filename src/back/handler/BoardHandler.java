package back.handler;

import back.ResponseCode;
import back.dao.chatting.JoinChattingRoomDAO;
import back.dao.board.PostingDAO;
import back.dao.GetInfoDAO;

import back.request.board.DeleteBoardRequest;
import back.request.board.EditBoardRequest;
import back.request.board.PostBoardRequest;
import back.request.chatroom.JoinChatRoomRequest;
import back.response.chatroom.JoinChatRoomResponse;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class BoardHandler extends Thread {
    private Socket clientSocket = null;
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;

    public BoardHandler(Socket clientSocket) {
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

            if (readObj instanceof PostBoardRequest postBoardRequest) {
                postBoardMethod(postBoardRequest);
            } else if (readObj instanceof EditBoardRequest editBoardRequest) {
                editBoardMethod(editBoardRequest);
            } else if (readObj instanceof DeleteBoardRequest deleteBoardRequest) {
                deleteBoardMethod(deleteBoardRequest);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /*게시글 생성 함수*/
    private void postBoardMethod(PostBoardRequest postBoardRequest) {
        try {
            PostingDAO postingDAO = new PostingDAO();
            GetInfoDAO getInfoDAO = new GetInfoDAO();
            JoinChattingRoomDAO joinChattingRoomDAO = new JoinChattingRoomDAO();

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
                    int port = joinChattingRoomDAO.assignChatRoomPort(); // 랜덤한 채팅방 포트를 할당한다.
                    postingDAO.posting(postBoardRequest, port); // 게시글 생성
                    objectOutputStream.writeObject(ResponseCode.POST_BOARD_SUCCESS); // 게시글 생성 성공 응답을 보낸다.

                    Object readObj = objectInputStream.readObject(); // 채팅방 입장 요청을 받는다.

                    if (readObj instanceof JoinChatRoomRequest joinChatRoomRequest) { // 채팅방 입장 요청일 경우
                        String nickName = getInfoDAO.getnickNameMethod(joinChatRoomRequest.uuid()); // uuid에 일치하는 닉네임을 가져옴

                        if (nickName != null) {
                            objectOutputStream.writeObject(ResponseCode.JOIN_CHATROOM_SUCCESS);
                            objectOutputStream.writeObject(new JoinChatRoomResponse(nickName, port)); // 닉네임, 채팅방 port 정보를 보낸다.
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
    private void editBoardMethod(EditBoardRequest editBoardRequest) {

    }

    /*게시글 삭제 함수*/
    private void deleteBoardMethod(DeleteBoardRequest deleteBoardRequest) {

    }
}
