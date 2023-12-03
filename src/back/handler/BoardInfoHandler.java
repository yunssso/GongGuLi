package back.handler;

import back.dao.board.SearchBoardDAO;
import back.request.board.SearchBoardInfoRequest;
import back.response.ResponseCode;
import back.dao.board.PrintBoardDAO;
import back.dao.board.ReadPostDAO;
import back.request.board.BoardInfoRequest;
import back.request.mypage.MyBoardInfoMoreRequest;
import back.request.board.BoardInfoMoreRequest;
import back.response.board.BoardInfoMoreResponse;
import back.response.board.BoardInfoResponse;
import back.response.mypage.MyBoardInfoMoreResponse;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class BoardInfoHandler extends Thread {
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;

    public BoardInfoHandler(Socket clientSocket) {
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

            if (readObj instanceof BoardInfoRequest boardInfoRequest) {
                boardInfoMethod(boardInfoRequest);
            } else if (readObj instanceof BoardInfoMoreRequest boardInfoMoreRequest) {
                boardInfoMoreMethod(boardInfoMoreRequest);
            } else if (readObj instanceof MyBoardInfoMoreRequest myBoardInfoMoreRequest) {
                myBoardInfoMoreMethod(myBoardInfoMoreRequest);
            } else if (readObj instanceof SearchBoardInfoRequest searchBoardInfoRequest) {
                searchBoardInfoMethod(searchBoardInfoRequest);
            }
        } catch (Exception exception) {
            //exception.printStackTrace();

            try {
                objectInputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /*게시글 갱신 Response를 보내는 메소드*/
    private void boardInfoMethod(BoardInfoRequest boardInfoRequest) {
        try {
            PrintBoardDAO printBoardDAO = new PrintBoardDAO();

            List<BoardInfoResponse> boardList = printBoardDAO.printBoard(boardInfoRequest.region(), boardInfoRequest.category());

            if (boardList == null) {
                objectOutputStream.writeObject(ResponseCode.BOARD_INFO_FAILURE);
            } else {
                objectOutputStream.writeObject(ResponseCode.BOARD_INFO_SUCCESS);

                objectOutputStream.writeObject(boardList);
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

    /*게시글 자세히 보기 (타인) Response를 보내는 메소드*/
    private void boardInfoMoreMethod(BoardInfoMoreRequest boardInfoMoreRequest) {
        try {
            ReadPostDAO readPostDAO = new ReadPostDAO();

            BoardInfoMoreResponse boardInfoMoreResponse = readPostDAO.readMorePost(boardInfoMoreRequest.selectRow(), boardInfoMoreRequest.uuid());

            if (boardInfoMoreResponse == null) {
                objectOutputStream.writeObject(ResponseCode.BOARD_INFO_MORE_FAILURE);
            } else {
                objectOutputStream.writeObject(ResponseCode.BOARD_INFO_MORE_SUCCESS);

                objectOutputStream.writeObject(boardInfoMoreResponse);
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

    /*게시글 자세히 보기 (자신) Response를 보내는 메소드*/
    private void myBoardInfoMoreMethod(MyBoardInfoMoreRequest myBoardInfoMoreRequest) {
        try {
            ReadPostDAO readPostDAO = new ReadPostDAO();

            MyBoardInfoMoreResponse myBoardInfoMoreResponse = readPostDAO.readMoreMyPost(myBoardInfoMoreRequest.selectRow(), myBoardInfoMoreRequest.uuid());

            if (myBoardInfoMoreResponse == null) {
                objectOutputStream.writeObject(ResponseCode.BOARD_INFO_MORE_FAILURE);
            } else {
                objectOutputStream.writeObject(ResponseCode.BOARD_INFO_MORE_SUCCESS);

                objectOutputStream.writeObject(myBoardInfoMoreResponse);
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

    private void searchBoardInfoMethod(SearchBoardInfoRequest searchBoardInfoRequest) {
        try {
            // searchBoardInfoRequest안에 필터 항목은 "제목", "글쓴이" 이고 Text는 내용이야 이거 사용해서 DAO 연결해줘
            // ResponseCode는 밑에거 활용해
            System.out.println(searchBoardInfoRequest.searchFilter());
            System.out.println(searchBoardInfoRequest.searchText());
            System.out.println(searchBoardInfoRequest.uuid());
            System.out.println(searchBoardInfoRequest.region());
            System.out.println(searchBoardInfoRequest.category());

            SearchBoardDAO searchBoardDAO = new SearchBoardDAO();
            List<BoardInfoResponse> list = searchBoardDAO.searchBoard(searchBoardInfoRequest);
            if (list == null) {
                objectOutputStream.writeObject(ResponseCode.SEARCH_BOARD_INFO_FAILURE);
            } else {
                objectOutputStream.writeObject(ResponseCode.SEARCH_BOARD_INFO_SUCCESS);
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