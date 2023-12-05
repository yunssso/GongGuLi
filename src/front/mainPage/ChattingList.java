package front.mainPage;

import back.request.chatroom.JoinChatRoomListRequest;
import back.request.chatroom.JoinChatRoomRequest;
import back.response.ResponseCode;
import back.request.chatroom.GetChattingRoomRequest;
import back.response.chatroom.GetChattingRoomResponse;
import back.response.chatroom.JoinChatRoomListResponse;
import front.ChatClient;
import front.FrontSetting;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        chattingListLabel.setBounds(10, 10, 100, 60);

        getChattingRoomMethod();

        DefaultTableModel model = new DefaultTableModel(chattingRoomListDB, fs.chattinglistHeader);
        JTable listTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 모든 셀을 수정 불가능으로 설정
            }
        };
        fs.tableSetting(listTable,fs.chattingListWidths);

        listTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // 더블 클릭 감지
                    int selectedRow = listTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // chattingRoomListDB[selectedRow]에서 정보를 사용하여 채팅방 열기
                        openChatRoom(selectedRow);
                    }
                }
            }
        });

        // 행 정렬기 자동 생성 비활성화
        listTable.setAutoCreateRowSorter(false);

        JScrollPane listScrollPane = new JScrollPane(listTable);
        listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScrollPane.setBounds(30, 80, 340, 430);

        chattingListPanel.add(chattingListLabel);
        chattingListPanel.add(listScrollPane);
        chattingListFrame.add(chattingListPanel);
        chattingListFrame.setVisible(true);
    }

    private void openChatRoom(int selectRow) {
        try (Socket clientSocket = new Socket("localhost", 1026);
             OutputStream outputStream = clientSocket.getOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
             InputStream inputStream = clientSocket.getInputStream();
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ){
            JoinChatRoomListRequest joinChatRoomListRequest = new JoinChatRoomListRequest(selectRow, uuid);

            objectOutputStream.writeObject(joinChatRoomListRequest);

            ResponseCode responseCode = (ResponseCode) objectInputStream.readObject();

            if (responseCode.getKey() == ResponseCode.JOIN_CHATROOM_SUCCESS.getKey()) {
                JoinChatRoomListResponse joinChatRoomListResponse = (JoinChatRoomListResponse) objectInputStream.readObject();

                new ChatClient(joinChatRoomListResponse.port(), uuid);
                // 채팅 목록에서 클릭시 채팅방 열림
            } else if (responseCode.getKey() == ResponseCode.JOIN_CHATROOM_FAILURE.getKey()) {
                fs.showErrorDialog(responseCode.getValue());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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
                List<GetChattingRoomResponse> getChattingRoomResponse = (List<GetChattingRoomResponse>) objectInputStream.readObject();

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
