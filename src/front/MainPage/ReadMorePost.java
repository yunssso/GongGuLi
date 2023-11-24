package front.MainPage;

import back.ResponseCode;
import back.request.chatroom.Join_ChatRoom_Request;
import back.response.board.Board_Info_More_Response;
import back.response.chatroom.Join_ChatRoom_Response;
import front.ChatClient;
import front.FrontSetting;
import front.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ReadMorePost {
    FrontSetting frontSetting = new FrontSetting();
    private String uuid = null;
    private int selectRow = 0;
    private JTable t;
    private Board_Info_More_Response boardInfoMoreResponse;

    public ReadMorePost(String uuid, JTable t, Board_Info_More_Response boardInfoMoreResponse) {
        this.uuid = uuid;
        this.t = t;
        this.boardInfoMoreResponse = boardInfoMoreResponse;
        readMorePost();
    }

    public ReadMorePost(String uuid, JTable t, int selectRow, Board_Info_More_Response boardInfoMoreResponse) {
        this.uuid = uuid;
        this.t = t;
        this.selectRow = selectRow;
        this.boardInfoMoreResponse =boardInfoMoreResponse;
        readMoreMyPost();
    }

    /*테이블 값 더블 클릭 시 자세히 보기(작성자 타인)*/
    public void readMorePost() {
        JFrame readMoreFrame = new JFrame(boardInfoMoreResponse.title());  // 자세히보기 팝업창 프레임
        readMoreFrame.setSize(500, 600);
        frontSetting.FrameSetting(readMoreFrame);

        Container c = readMoreFrame.getContentPane();  // 자세히보기 팝업창 패널
        c.setLayout(null);
        c.setBackground(frontSetting.mainColor);

        JLabel logoLabel = new JLabel("공구리");  // 라벨
        logoLabel.setFont(frontSetting.fb20);
        logoLabel.setBounds(220, 20, 100, 40);

        JTextArea titleArea = new JTextArea(" 제목: " + boardInfoMoreResponse.title());
        titleArea.setBounds(20, 80, 445, 35);
        titleArea.setFont(frontSetting.f18);
        titleArea.setEditable(false);

        JTextArea infoArea1 = new JTextArea(" 지역: " + boardInfoMoreResponse.region() +
                "\n 글쓴이: " + boardInfoMoreResponse.nickName());
        infoArea1.setBounds(20, 125, 230, 55);
        infoArea1.setFont(frontSetting.f18);
        infoArea1.setEditable(false);

        JTextArea infoArea2 = new JTextArea("카테고리: " + boardInfoMoreResponse.category() +
                "\n현황: " + boardInfoMoreResponse.peopleNum());
        infoArea2.setBounds(250, 125, 215, 55);
        infoArea2.setFont(frontSetting.f18);
        infoArea2.setEditable(false);

        JTextArea contentArea = new JTextArea(" " + boardInfoMoreResponse.content());
        contentArea.setBounds(20, 210, 445, 250);
        contentArea.setFont(frontSetting.f18);
        contentArea.setEditable(false);
        contentArea.setDragEnabled(false);

        JLabel viewCountLabel = new JLabel("조회수: " + boardInfoMoreResponse.view());
        viewCountLabel.setFont(frontSetting.f14);
        viewCountLabel.setBounds(20, 465, 150, 20);

        RoundedButton joinChatRoomBtn = new RoundedButton("채팅 참여");
        joinChatRoomBtn.setBounds(190, 480, 110, 50);
        joinChatRoomBtn.setFont(frontSetting.fb16);

        /*채팅 참여 버튼 클릭시*/
        joinChatRoomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Socket clientSocket = new Socket("localhost", 1026);
                     OutputStream outputStream = clientSocket.getOutputStream();
                     ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                     InputStream inputStream = clientSocket.getInputStream();
                     ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                ){

                    int selectRow = 0; //여기에 사용자가 선택한 게시글 id를 받아와야 돼
                    Join_ChatRoom_Request joinChatroomRequest = new Join_ChatRoom_Request(selectRow, uuid);

                    objectOutputStream.writeObject(joinChatroomRequest);

                    ResponseCode responseCode = (ResponseCode) objectInputStream.readObject();

                    if (responseCode.getKey() == ResponseCode.JOIN_CHATROOM_SUCCESS.getKey()) { //채팅방 입장 성공
                        Join_ChatRoom_Response joinChatRoomResponse = (Join_ChatRoom_Response) objectInputStream.readObject();

                        new ChatClient(joinChatRoomResponse.nickName(), joinChatRoomResponse.chatPort(), uuid);
                    } else { //채팅방 입장 실패
                        showErrorDialog(responseCode.getValue());
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        c.add(logoLabel);
        c.add(titleArea);
        c.add(infoArea1);
        c.add(infoArea2);
        c.add(contentArea);
        c.add(joinChatRoomBtn);
        c.add(viewCountLabel);

        readMoreFrame.setVisible(true);
    }

    /*테이블 값 더블 클릭 시 자세히보기(작성자 본인)*/
    public void readMoreMyPost() {
        JFrame readMoreFrame = new JFrame(boardInfoMoreResponse.title());  // 자세히보기 팝업창 프레임
        readMoreFrame.setSize(500, 600);
        frontSetting.FrameSetting(readMoreFrame);

        Container c = readMoreFrame.getContentPane();  // 자세히보기 팝업창 패널
        c.setLayout(null);
        c.setBackground(frontSetting.mainColor);

        JLabel logoLabel = new JLabel("공구리");  // 라벨
        logoLabel.setFont(frontSetting.fb20);
        logoLabel.setBounds(220, 20, 100, 40);

        JTextArea titleArea = new JTextArea(" 제목: " + boardInfoMoreResponse.title());
        titleArea.setBounds(20, 80, 445, 35);
        titleArea.setFont(frontSetting.f18);
        titleArea.setEditable(false);

        JTextArea infoArea1 = new JTextArea(" 지역: " + boardInfoMoreResponse.region() +
                "\n 현황: " + boardInfoMoreResponse.peopleNum());
        infoArea1.setBounds(20, 125, 230, 55);
        infoArea1.setFont(frontSetting.f18);
        infoArea1.setEditable(false);

        JTextArea infoArea2 = new JTextArea("카테고리: " + boardInfoMoreResponse.category());
        infoArea2.setBounds(250, 125, 215, 55);
        infoArea2.setFont(frontSetting.f18);
        infoArea2.setEditable(false);

        JTextArea contentArea = new JTextArea(boardInfoMoreResponse.content());
        contentArea.setBounds(20, 210, 445, 250);
        contentArea.setFont(frontSetting.f18);
        contentArea.setEditable(false);
        contentArea.setDragEnabled(false);

        JLabel viewCountLabel = new JLabel("조회수: " + boardInfoMoreResponse.view());
        viewCountLabel.setFont(frontSetting.f14);
        viewCountLabel.setBounds(20, 465, 150, 20);

        RoundedButton modifyPostBtn = new RoundedButton("수정하기");
        modifyPostBtn.setBounds(190, 480, 110, 50);
        modifyPostBtn.setFont(frontSetting.fb16);

        modifyPostBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readMoreFrame.dispose();
                new ModifyMyPost();
            }
        });

        JButton deletePostBtn = new JButton("삭제하기");
        deletePostBtn.setBounds(400, 465, 70, 20);
        deletePostBtn.setBackground(null);
        deletePostBtn.setBorder(null);
        deletePostBtn.setFont(frontSetting.f14);
        deletePostBtn.setForeground(frontSetting.c3);

        deletePostBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int answer = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    System.out.println("삭제");
                } else System.out.println("삭제 안함");
            }
        });

        c.add(logoLabel);
        c.add(titleArea);
        c.add(infoArea1);
        c.add(infoArea2);
        c.add(contentArea);
        c.add(modifyPostBtn);
        c.add(deletePostBtn);
        c.add(viewCountLabel);

        readMoreFrame.setVisible(true);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "안내", JOptionPane.ERROR_MESSAGE);
    }
}
