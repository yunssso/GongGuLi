package back.handler;

import back.ResponseCode;
import back.dao.BoardDAO;
import back.request.Board_Info_Request;
import back.request.Join_ChatRoom_Request;
import back.response.Board_Info_Response;
import back.response.Join_ChatRoom_Response;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class ChatRoom_Handler extends Thread {
    private Socket clientSocket = null;

    private OutputStream os = null;
    private ObjectOutputStream oos = null;

    private InputStream is = null;
    private ObjectInputStream ois = null;

    private final BoardDAO boardDAO = new BoardDAO();

    public ChatRoom_Handler(Socket clientSocket) {
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

            closeHandler();
        } catch (Exception exception) {
            closeHandler();
            exception.printStackTrace();
        }
    }

    private void joinChatRoomMethod(Object readObj) {
        try {
            Join_ChatRoom_Request joinChatRoomRequest = (Join_ChatRoom_Request) readObj;

            //사용자에게 접속을 원하는 게시글 id, uuid 정보를 받아와서 처리할 거 처리하고 포트 정보 및 대화 내용 등 return 해주는 DAO 필요
            Join_ChatRoom_Response joinChatRoomResponse = new Join_ChatRoom_Response(8888); //<- 여기에 포트를 DAO에서 받아와야 함

            if (joinChatRoomResponse == null) {
                oos.writeObject(ResponseCode.JOIN_CHATROOM_FAILURE);
            } else {
                oos.writeObject(ResponseCode.JOIN_CHATROOM_SUCCESS);
                oos.writeObject(joinChatRoomResponse);
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
