package back.handler;

import back.ChatServer;
import back.ResponseCode;
import back.dao.BoardDAO;
import back.request.Post_Board_Request;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Board_Handler extends Thread {
    private Socket clientSocket = null;
    private ServerSocket serversocket = null;

    private OutputStream os = null;
    private ObjectOutputStream oos = null;

    private InputStream is = null;
    private ObjectInputStream ois = null;

    private final BoardDAO boardDAO = new BoardDAO();

    private static final int MIN_PORT = 1029; //1029 ~ 1029는 소켓으로 GET POST를 위해서 사용할 예정
    private static final int MAX_PORT = 49151;

    private int port = 0;

    public Board_Handler(Socket clientSocket) {
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

            if (readObj instanceof Post_Board_Request) {
                postBoardMethod(readObj);
            }
        } catch (Exception exception) {
            closeHandler();
            exception.printStackTrace();
        }
    }

    private void postBoardMethod(Object readObj) {
        try {
            //클라이언트에서 게시글 생성 요청을 받는다.
            Post_Board_Request postBoardRequest = (Post_Board_Request) readObj;

            //각 조건들을 비교하여 클라이언트에 응답을 보낸다.
            if (postBoardRequest.title().isBlank()) {
                oos.writeObject(ResponseCode.TITLE_MISSING);
            } else if (postBoardRequest.region().equals(" --")) {
                oos.writeObject(ResponseCode.REGION_NOT_SELECTED);
            } else if (postBoardRequest.category().equals(" --")) {
                oos.writeObject(ResponseCode.CATEGORY_NOT_SELECTED);
            } else if (postBoardRequest.peopleNum().isBlank()) {
                oos.writeObject(ResponseCode.PEOPLE_NUM_MISSING);
            } else if (postBoardRequest.content().isBlank()) {
                oos.writeObject(ResponseCode.CONTENT_MISSING);
            } else {
                if (Integer.parseInt(postBoardRequest.peopleNum()) > 30) {
                    oos.writeObject(ResponseCode.PEOPLE_NUM_OVER_LIMIT);
                } else if (Integer.parseInt(postBoardRequest.peopleNum()) <= 1) {
                    oos.writeObject(ResponseCode.PEOPLE_NUM_UNDER_LIMIT);
                } else {
                    while (true) {
                        port = getRandomPortInRange(MIN_PORT, MAX_PORT);

                        try {
                            serversocket = new ServerSocket(port);
                            break; // 유효한 포트를 찾으면 루프 종료
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                    new ChatServer(serversocket);
                    boardDAO.posting(postBoardRequest, port);
                    oos.writeObject(ResponseCode.POST_BOARD_SUCCESS);
                }
            }

            closeHandler();
        } catch (NumberFormatException numberFormatException) {
            try {
                oos.writeObject(ResponseCode.PEOPLE_NUM_NOT_NUMBER);

                closeHandler();
            } catch (Exception exception) {
                closeHandler();
                exception.printStackTrace();
            }
        } catch (Exception exception) {
            closeHandler();
            exception.printStackTrace();
        }
    }

    private void editBoardMethod(Object readObj) {

    }

    private static int getRandomPortInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
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
