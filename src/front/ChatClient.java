package front;

import back.ResponseCode;
import back.request.chatroom.Kick_ChatRoom_Request;
import back.request.chatroom.Message_ChatRoom_Request;
import back.response.chatroom.Message_ChatRoom_Response;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class ChatClient extends JFrame implements Runnable{
    private Color c1 = new Color(255, 240, 227);
    private Color c3 = new Color(255, 255, 255);

    private ObjectOutputStream objectOutputStream = null;

    private ObjectInputStream objectInputStream = null;

    //Main Frame
    private JPanel chattingListPanel = null;
    private JTextField tf = null;
    private JTextArea chatTextArea = null;
    private JScrollPane scrollPane = null;

    //Participants Frame
    private JButton participantsButton = null;
    private JFrame participantsFrame = null;
    private JList<String> participantsList = null;
    private DefaultListModel<String> participantListModel;
    private JScrollPane participantsscrollPane = null;

    //클라이언트 사용자 이름
    private String nickName = null;

    //서버에서 각 클라이언트 이름을 받아오는 리스트
    private ArrayList<String> nameList = null;
    private String uuid = null;

    //처음 클라이언트가 생성되면 자동으로 로그인 메소드부터 실행 되도록 구현
    public ChatClient(String nickName, int port, String uuid) {
        try {
            this.nickName = nickName;
            //포트 정보
            this.uuid = uuid;

            createAndShowGUI();

            Socket socket = new Socket("localhost", port);
            //43.200.49.16

            //서버 -> 클라이언트 Output Stream
            //통신 관련
            OutputStream outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);

            //서버 <- 클라이언트 Input Stream
            InputStream inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);

            objectOutputStream.writeObject(new Message_ChatRoom_Request(nickName, null, uuid));

            Thread thread = new Thread(this);
            thread.start();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void createAndShowGUI() {
        setTitle("채팅 목록");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        chattingListPanel = new JPanel(new BorderLayout());

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false); // 편집 불가능하도록 설정
        chatTextArea.setForeground(Color.BLACK);
        scrollPane = new JScrollPane(chatTextArea);

        tf = new JTextField("");
        tf.setBounds(20, 500, 280, 50);
        tf.setBackground(c1);

        //Enter를 입력할 경우에 sendMessage 실행
        tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // 참가자 문구 생성
        participantsButton = new JButton("참가자 : 0");
        participantsButton.setHorizontalAlignment(JButton.RIGHT);

        participantsButton.setBackground(c1);

        participantsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showParticipants();
            }
        });

        chattingListPanel.add(participantsButton, BorderLayout.NORTH);
        chattingListPanel.add(scrollPane, BorderLayout.CENTER);
        chattingListPanel.add(tf, BorderLayout.SOUTH);
        add(chattingListPanel);

        setVisible(true);
    }

    //Participants Frame
    private void showParticipants() {
        participantsFrame = new JFrame();
        participantsFrame.setLayout(new BorderLayout());
        participantsFrame.setSize(150, 300);
        participantsFrame.setLocationRelativeTo(null);
        participantsFrame.setResizable(false);

        participantListModel = new DefaultListModel<>();

        for (String n : nameList) {
            participantListModel.addElement(n);
        }

        participantsList = new JList<>(participantListModel);

        participantsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    if (!e.getValueIsAdjusting()) {
                        String selected_name = participantsList.getSelectedValue();
                        if (selected_name != null) {
                            kickRequest(selected_name);
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        participantsscrollPane = new JScrollPane(participantsList);

        participantsFrame.add(participantsscrollPane, BorderLayout.CENTER);

        participantsFrame.setVisible(true);
    }

    //메세지를 서버에서 받아오는 함수
    @Override
    public void run() {
        try {
            while(true) {
                if (objectInputStream != null) {
                    Object readObj = objectInputStream.readObject();

                    if (readObj instanceof Message_ChatRoom_Response) {
                        Message_ChatRoom_Response messageChatRoomResponse = (Message_ChatRoom_Response) readObj;

                        chatTextArea.append(messageChatRoomResponse.nickName() + " : " + messageChatRoomResponse.message() + "\n");
                    }

                    int pos = chatTextArea.getText().length();
                    chatTextArea.setCaretPosition(pos);
                } else break;
            }
        } catch(Exception exception) {
            chatTextArea.append("서버 통신 오류...");
        }
    }

    //메세지를 서버로 전송하는 함수
    private void sendMessage() {
        try {
            String message = tf.getText();

            if (objectOutputStream != null) {
                Message_ChatRoom_Request messageChatRoomRequest = new Message_ChatRoom_Request(nickName, message, uuid);

                objectOutputStream.writeObject(messageChatRoomRequest);

                tf.setText("");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void kickRequest(String selected_name) {
        try {
            if (objectOutputStream != null) {
                Kick_ChatRoom_Request kickChatRoomRequest = new Kick_ChatRoom_Request(selected_name, uuid);

                objectOutputStream.writeObject(kickChatRoomRequest);

                ResponseCode responseCode = (ResponseCode) objectInputStream.readObject();

                if (responseCode.getKey() == ResponseCode.KICK_CHATROOM_SUCCESS.getKey()) {
                    // 성공적으로 강제퇴장을 했을 경우
                } else if (responseCode.getKey() == ResponseCode.KICK_CHATROOM_FAILURE.getKey()) {
                    // 방장 권한이 없어서 강제퇴장을 못 했을 경우
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}