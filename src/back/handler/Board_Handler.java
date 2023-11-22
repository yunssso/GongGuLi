package back.handler;

import back.ChatServer;
import back.ResponseCode;
import back.dao.ChatRoomDAO;
import back.dao.PostingDAO;
import back.request.board.Delete_Board_Request;
import back.request.board.Edit_Board_Request;
import back.request.board.Post_Board_Request;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Board_Handler extends Thread {
    private Socket clientSocket = null;
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;

    private final PostingDAO posting = new PostingDAO();
    private final ChatRoomDAO chatRoomDAO = new ChatRoomDAO();


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
                chatRoomDAO.editBoardMethod(editBoardRequest);
            } else if (readObj instanceof Delete_Board_Request deleteBoardRequest) {
                chatRoomDAO.deleteBoardMethod(deleteBoardRequest);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

//    게시글 생성 함수
    private void postBoardMethod(Post_Board_Request postBoardRequest) {
        try {
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
                    int port = chatRoomDAO.assignChatRoomPort();
                    posting.posting(postBoardRequest, port);
                    objectOutputStream.writeObject(ResponseCode.POST_BOARD_SUCCESS);
                }
            }
        } catch (NumberFormatException numberFormatException) {
            try (OutputStream outputStream = clientSocket.getOutputStream();
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            ) {
                objectOutputStream.writeObject(ResponseCode.PEOPLE_NUM_NOT_NUMBER);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
