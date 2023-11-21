package back.handler;

import back.ResponseCode;
import back.dao.BoardDAO;
import back.request.Board_Info_Request;
import back.response.Board_Info_Response;
import back.request.Board_Info_More_Request;
import back.response.Board_Info_More_Response;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class Board_Info_Handler extends Thread {
    private Socket clientSocket = null;

    private OutputStream os = null;
    private ObjectOutputStream oos = null;

    private InputStream is = null;
    private ObjectInputStream ois = null;

    private final BoardDAO boardDAO = new BoardDAO();

    public Board_Info_Handler(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;

            //서버 -> 클라이언트 Output Stream
            os = clientSocket.getOutputStream();
            oos = new ObjectOutputStream(os);

            //서버 <- 클라이언트 Input Stream
            is = clientSocket.getInputStream();
            ois = new ObjectInputStream(is);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Object readObj = ois.readObject();

            if (readObj instanceof Board_Info_Request) {
                boardInfoMethod(readObj);
            } else if (readObj instanceof Board_Info_More_Request) {
                boardInfoMoreMethod(readObj);
            }

            closeHandler();
        } catch (Exception exception) {
            closeHandler();
            exception.printStackTrace();
        }
    }

    // 게시판에 있는 글 갱신 해오는 메소드
    private void boardInfoMethod(Object readObj) {
        try {
            Board_Info_Request boardInfoRequest = (Board_Info_Request) readObj;

            List boardList = boardDAO.printBoard(boardInfoRequest.region(), boardInfoRequest.category());

            if (boardList == null) {
                oos.writeObject(ResponseCode.BOARD_INFO_FAILURE);
            } else {
                oos.writeObject(ResponseCode.BOARD_INFO_SUCCESS);
                oos.writeObject(boardList);
            }

            closeHandler();
        } catch (Exception exception) {
            exception.printStackTrace();
            closeHandler();
        }
    }

    // 게시글을 클릭 했을때 자세히 보기를 하는 메소드
    private void boardInfoMoreMethod(Object readObj) {
        try {
            Board_Info_More_Request boardInfoMoreRequest = (Board_Info_More_Request) readObj;

            Board_Info_More_Response boardInfoMoreResponse = boardDAO.readMorePost(boardInfoMoreRequest.selectRow());

            if (boardInfoMoreResponse == null) {
                oos.writeObject(ResponseCode.BOARD_INFO_MORE_FAILURE);
            } else {
                oos.writeObject(ResponseCode.BOARD_INFO_MORE_SUCCESS);
                oos.writeObject(boardInfoMoreResponse);
            }

            closeHandler();
        } catch (Exception exception) {
            exception.printStackTrace();
            closeHandler();
        }
    }

    private void closeHandler() {
        try {
            oos.close();
            os.close();

            ois.close();
            is.close();

            clientSocket.close();
        } catch(Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                oos.close();
                os.close();

                ois.close();
                is.close();

                clientSocket.close();
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}