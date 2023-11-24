package back.handler;

import back.ResponseCode;
import back.dao.PrintBoardDAO;
import back.dao.ReadPostDAO;
import back.request.board.Board_Info_Request;
import back.request.mypage.My_Board_Info_More_Request;
import back.request.board.Board_Info_More_Request;
import back.response.board.Board_Info_More_Response;
import back.response.mypage.My_Board_Info_More_Response;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class Board_Info_Handler extends Thread {
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;

    private final PrintBoardDAO boardDAO = new PrintBoardDAO();
    private final ReadPostDAO readPostDAO = new ReadPostDAO();

    public Board_Info_Handler(Socket clientSocket) {
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

            if (readObj instanceof Board_Info_Request boardInfoRequest) {
                boardInfoMethod(boardInfoRequest);
            } else if (readObj instanceof Board_Info_More_Request boardInfoMoreRequest) {
                boardInfoMoreMethod(boardInfoMoreRequest);
            } else if (readObj instanceof My_Board_Info_More_Request myBoardInfoMoreRequest) {
                myBoardInfoMoreMethod(myBoardInfoMoreRequest);
            }
        } catch (Exception exception) {
            //exception.printStackTrace(); <- 여기서 계속 이상한 버그 터지는데 무시해도 될 듯
        }
    }

    // 게시판에 있는 글 갱신 해오는 메소드
    private void boardInfoMethod(Board_Info_Request boardInfoRequest) {
        try {
            List boardList = boardDAO.printBoard(boardInfoRequest.region(), boardInfoRequest.category());

            if (boardList == null) {
                objectOutputStream.writeObject(ResponseCode.BOARD_INFO_FAILURE);
            } else {
                objectOutputStream.writeObject(ResponseCode.BOARD_INFO_SUCCESS);
                objectOutputStream.writeObject(boardList);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // 게시글을 클릭 했을때 자세히 보기를 하는 메소드
    private void boardInfoMoreMethod(Board_Info_More_Request boardInfoMoreRequest) {
        try {
            Board_Info_More_Response boardInfoMoreResponse = readPostDAO.readMorePost(boardInfoMoreRequest.selectRow(), boardInfoMoreRequest.uuid());

            if (boardInfoMoreResponse == null) {
                objectOutputStream.writeObject(ResponseCode.BOARD_INFO_MORE_FAILURE);
            } else {
                objectOutputStream.writeObject(ResponseCode.BOARD_INFO_MORE_SUCCESS);
                objectOutputStream.writeObject(boardInfoMoreResponse);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // 내가 쓴 게시글을 클릭 했을때 자세히 보기를 하는 메소드
    private void myBoardInfoMoreMethod(My_Board_Info_More_Request myBoardInfoMoreRequest) {
        try {
            My_Board_Info_More_Response myBoardInfoMoreResponse = readPostDAO.readMoreMyPost(myBoardInfoMoreRequest.selectRow());

            if (myBoardInfoMoreResponse == null) {
                objectOutputStream.writeObject(ResponseCode.BOARD_INFO_MORE_FAILURE);
            } else {
                objectOutputStream.writeObject(ResponseCode.BOARD_INFO_MORE_SUCCESS);
                objectOutputStream.writeObject(myBoardInfoMoreRequest);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}