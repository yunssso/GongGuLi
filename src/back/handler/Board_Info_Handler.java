package back.handler;

import back.ResponseCode;
import back.dao.BoardDAO;
import back.request.Board_Info_Request;
import back.response.Board_Info_Response;

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
            Board_Info_Request boardInfoRequest = (Board_Info_Request) readObj;

            List <Board_Info_Response> boardList = boardDAO.printBoard(boardInfoRequest.region(), boardInfoRequest.category(), boardInfoRequest.uuid());

            if (!boardList.isEmpty()) {
                oos.writeObject(ResponseCode.BOARD_INFO_SUCCESS);
                oos.writeObject(boardList);
            } else {
                oos.writeObject(ResponseCode.BOARD_INFO_FAILURE);
            }

            CloseHandler();
        } catch (Exception exception) {
            CloseHandler();
            exception.printStackTrace();
        }
    }

    private void CloseHandler() {
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
