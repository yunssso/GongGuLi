package front.mainPage;

import back.ResponseCode;
import back.request.chatroom.GetChattingRoomRequest;
import back.response.chatroom.GetChattingRoomResponse;
import front.FrontSetting;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class ChattingList {
    String uuid;

    private String[][] chattingRoomListDB = null;  // 차후 DB 연동
    FrontSetting fs = new FrontSetting();

    public ChattingList(String uuid) {
        this.uuid = uuid;
        setChattingList();
    }

    private void setChattingList() {
        JFrame chattingListFrame = new JFrame();  // 채팅 목록 팝업창 프레임
        chattingListFrame.setTitle("채팅 목록");
        chattingListFrame.setSize(400, 600);
        fs.FrameSetting(chattingListFrame);

        JPanel chattingListPanel = new JPanel(null);  // 채팅 목록 팝업창 패널
        chattingListPanel.setBounds(0, 0, 400, 600);
        chattingListPanel.setBackground(fs.mainColor);

        JLabel chattingListLabel = new JLabel("채팅 목록");  // 채팅 목록 레이블
        chattingListLabel.setFont(new Font("SUITE", Font.BOLD, 20));
        chattingListPanel.setBounds(10, 10, 100, 60);

        getChattingRoomMethod();
        JTable listTable = new JTable(chattingRoomListDB, fs.chattinglistHeader);

        JScrollPane listScrollPane = new JScrollPane(listTable);
        listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScrollPane.setBounds(30, 80, 340, 430);

        chattingListFrame.add(chattingListPanel);
        chattingListPanel.add(chattingListLabel);
        chattingListPanel.add(listScrollPane);

        chattingListFrame.setVisible(true);
    }

    private void getChattingRoomMethod() {
        try (Socket clientSocket = new Socket("localhost", 1026);
             OutputStream outputStream = clientSocket.getOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
             InputStream inputStream = clientSocket.getInputStream();
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ){
            //서버로 정보를 전달 해주기 위해서 객체 형식으로 변환
            GetChattingRoomRequest getChattingRoomRequest = new GetChattingRoomRequest(uuid);

            objectOutputStream.writeObject(getChattingRoomRequest);

            ResponseCode responseCode = (ResponseCode) objectInputStream.readObject();

            if (responseCode.getKey() == ResponseCode.GET_CHATROOM_SUCCESS.getKey()) { // 채팅방 목록 갱신 성공
                //  boardList안에 레코드 형태에 게시글 정보가 다 들어있음.
                java.util.List<GetChattingRoomResponse> getChattingRoomResponse = (List<GetChattingRoomResponse>) objectInputStream.readObject();

                setChattingRoomListDB(getChattingRoomResponse);
            } else { // 채팅방 목록 갱신 실패
                fs.showErrorDialog(responseCode.getValue());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //  2차원 배열로 변환
    public void setChattingRoomListDB(List<GetChattingRoomResponse> getChattingRoomResponse) {
        chattingRoomListDB = new String[getChattingRoomResponse.size()][5];

        for (int i = 0; i < getChattingRoomResponse.size(); i++) {
            GetChattingRoomResponse boardInfo = getChattingRoomResponse.get(i);
            chattingRoomListDB[i][0] = boardInfo.region();
            chattingRoomListDB[i][1] = boardInfo.category();
            chattingRoomListDB[i][2] = boardInfo.title();
            chattingRoomListDB[i][3] = boardInfo.writerUuid();
            chattingRoomListDB[i][4] = boardInfo.lastUpdatedTime();
        }
    }
}
