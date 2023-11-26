package back.handler;

import back.ResponseCode;
import back.dao.board.ModifyMyPostDAO;
import back.dao.chatting.JoinChattingRoomDAO;
import back.dao.board.PostingDAO;
import back.dao.GetInfoDAO;

import back.request.board.DeleteBoardRequest;
import back.request.board.ModifyMyPostRequest;
import back.request.board.PostBoardRequest;
import back.request.chatroom.JoinChatRoomRequest;
import back.response.chatroom.JoinChatRoomResponse;

import java.io.*;
import java.net.Socket;

public class BoardHandler extends Thread {
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;

    public BoardHandler(Socket clientSocket) {
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

    /*사용자 Request를 받는 메소드*/
    @Override
    public void run() {
        try {
            Object readObj = objectInputStream.readObject();

            if (readObj instanceof PostBoardRequest postBoardRequest) {
                postBoardMethod(postBoardRequest);
            } else if (readObj instanceof ModifyMyPostRequest modifyMyPostRequest) {
                modifyMyPostMethod(modifyMyPostRequest);
            } else if (readObj instanceof DeleteBoardRequest deleteBoardRequest) {
                deleteBoardMethod(deleteBoardRequest);
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

    /*게시글 생성 Reponse를 보내는 메소드*/
    private void postBoardMethod(PostBoardRequest postBoardRequest) {
        try {
            PostingDAO postingDAO = new PostingDAO();
            GetInfoDAO getInfoDAO = new GetInfoDAO();
            JoinChattingRoomDAO joinChattingRoomDAO = new JoinChattingRoomDAO();

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

                    if (postingDAO.posting(postBoardRequest, port)) {   //  DB로 게시글 생성 요청
                        objectOutputStream.writeObject(ResponseCode.POST_BOARD_SUCCESS); // 게시글 생성 성공 응답을 보낸다.
                    } else {
                        objectOutputStream.writeObject(ResponseCode.POST_BOARD_FAILURE);
                    }

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
        } finally {
            try {
                objectInputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /*게시글 수정 Response를 보내는 메소드*/
    private void modifyMyPostMethod(ModifyMyPostRequest modifyMyPostRequest) {
        try {
            ModifyMyPostDAO modifyMyPostDAO = new ModifyMyPostDAO();
            boolean isModified = modifyMyPostDAO.modifyMyPost(modifyMyPostRequest); //  DB로 수정 요청
            if (isModified) {
                objectOutputStream.writeObject(ResponseCode.MODIFY_MY_BOARD_SUCCESS); // 게시글 수정 성공 응답을 보낸다.
            } else {
                objectOutputStream.writeObject(ResponseCode.MODIFY_MY_BOARD_FAILURE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*게시글 삭제 Reponse를 보내는 메소드*/
    private void deleteBoardMethod(DeleteBoardRequest deleteBoardRequest) {

    }
}
