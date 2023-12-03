package back.handler;

import back.response.ResponseCode;
import back.dao.board.PrintBoardDAO;
import back.dao.user.AccountDAO;
import back.request.mypage.MyBoardInfoRequest;
import back.request.mypage.MyHistoryInfoRequest;
import back.request.mypage.UserInfoRequest;
import back.response.mypage.MyBoardInfoResponse;
import back.response.mypage.MyHistoryInfoResponse;
import back.response.mypage.UserInfoResponse;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class MyPageHandler extends Thread {
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;

    public MyPageHandler(Socket clientSocket) {
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

            if (readObj instanceof UserInfoRequest userInfoRequest) {
                sendUserInfo(userInfoRequest);
            } else if (readObj instanceof MyBoardInfoRequest myBoardInfoRequest) {
                sendMyBoardInfo(myBoardInfoRequest);
            } else if (readObj instanceof MyHistoryInfoRequest myHistoryInfoRequest) {
                sendMyHistoryInfo(myHistoryInfoRequest);
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

    /*내가 쓴 글 Response를 보내느 메소드*/
    private void sendMyBoardInfo(MyBoardInfoRequest myBoardInfoRequest) {
        try {
            PrintBoardDAO printBoardDAO = new PrintBoardDAO();

            List<MyBoardInfoResponse> list = printBoardDAO.printMyBoard(myBoardInfoRequest.uuid());

            if (list == null) {
                objectOutputStream.writeObject(ResponseCode.GET_MY_BOARD_INFO_FAILURE);
            } else {
                objectOutputStream.writeObject(ResponseCode.GET_MY_BOARD_INFO_SUCCESS);
                objectOutputStream.writeObject(list);
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

    /*유저정보 갱신 Response를 보내는 메소드*/
    private void sendUserInfo(UserInfoRequest userInfoRequest) {
        try {
            AccountDAO accountDAO = new AccountDAO();
            UserInfoResponse userInfoResponse = accountDAO.getMyInfo(userInfoRequest.uuid());

            if (userInfoResponse == null) {
                objectOutputStream.writeObject(ResponseCode.GET_USERINFO_FAILURE);
            } else {
                objectOutputStream.writeObject(ResponseCode.GET_USERINFO_SUCCESS);
                objectOutputStream.writeObject(userInfoResponse);
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

    /*공구내역 갱신 Response를 보내는 메소드*/
    private void sendMyHistoryInfo(MyHistoryInfoRequest myHistoryInfoRequest) {
        try {
            PrintBoardDAO printBoardDAO = new PrintBoardDAO();

            List<MyHistoryInfoResponse> list = printBoardDAO.printMyHistoryBoard(myHistoryInfoRequest.uuid());

            if (list == null) {
                objectOutputStream.writeObject(ResponseCode.GET_MY_HISTORY_INFO_FAILURE);
            } else {
                objectOutputStream.writeObject(ResponseCode.GET_MY_HISTORY_INFO_SUCCESS);
                objectOutputStream.writeObject(list);
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
