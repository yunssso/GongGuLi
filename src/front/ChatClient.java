package front;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class ChatClient extends JFrame implements Runnable {
    //통신 관련
    private BufferedReader in = null;
    private PrintWriter out = null;
    private Socket socket = null;

    //Main Frame
    private JPanel chattingListPanel = null;
    private JTextField tf = null;
    private JTextArea chatTextArea = null;
    private JScrollPane scrollPane = null;

    //Login Frame
    private JFrame loginFrame = null;
    private JLabel text = null;
    private JTextField textbox = null;

    //Participants Frame
    private JButton participantsButton = null;
    private JFrame participantsFrame = null;
    private JList<String> participantsList = null;
    private DefaultListModel<String> participantListModel;
    private JScrollPane participantsscrollPane = null;

    //클라이언트 사용자 이름
    private String name = null;
    //서버에서 각 클라이언트 이름을 받아오는 리스트
    private ArrayList<String> nameList = null;
    //방장 권리
    private boolean master = false;
    //포트 정보
    private int port;

    //처음 클라이언트가 생성되면 자동으로 로그인 메소드부터 실행 되도록 구현
    public ChatClient(int port) {
        this.port = port;
        login();
    }

    //Main Frame
    private void createAndShowGUI() {
        setTitle("채팅 목록");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chattingListPanel = new JPanel(new BorderLayout());

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false); // 편집 불가능하도록 설정
        chatTextArea.setForeground(Color.BLUE);
        scrollPane = new JScrollPane(chatTextArea);

        tf = new JTextField("");
        tf.setBounds(20, 500, 280, 50);

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

    //Login Frame
    private void login() {
        loginFrame = new JFrame();
        loginFrame.setLayout(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);

        text = new JLabel("Name");
        textbox = new JTextField();

        textbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = textbox.getText();
                loginFrame.dispose();
                createAndShowGUI();
                startClient(name);
            }
        });

        text.setBounds(40, -20, 100, 100);
        textbox.setBounds(80, 20, 160, 30);

        loginFrame.add(text);
        loginFrame.add(textbox);

        loginFrame.setVisible(true);
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
                if (!e.getValueIsAdjusting()) {
                    if (!master) {
                        JOptionPane.showMessageDialog(null, "권한이 없습니다.", "Confirm", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        String selected_name = participantsList.getSelectedValue();
                        if (selected_name != null) {
                            if (selected_name.equals(name)) {
                                JOptionPane.showMessageDialog(null, "자신을 강퇴할 수 없습니다.", "Confirm", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                int answer = JOptionPane.showConfirmDialog(null, "[" + selected_name + "] 강퇴 하시겠습니까?", "Confirm", JOptionPane.YES_NO_OPTION);

                                if (answer == JOptionPane.YES_OPTION) {
                                    kickoutUser(selected_name);
                                }
                            }
                        }
                    }
                }
            }
        });
        
        participantsscrollPane = new JScrollPane(participantsList);

        participantsFrame.add(participantsscrollPane, BorderLayout.CENTER);
        
        participantsFrame.setVisible(true);
    }

    //클라이언트 시작 함수
    private void startClient(String name) {
        try {
            socket = new Socket("localhost", 1084);
            //43.200.49.16
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            out.println(name);
            out.flush();

            Thread t = new Thread(this);
            t.start();
        } catch (IOException e) {
            chatTextArea.append("[서버 통신 오류]");
        }
    }
    
    //사용자를 강퇴 시키는 함수
    private void kickoutUser(String target_name) {
        out.println("KICKNAME:" + target_name);
        out.flush();
    }

    //메세지를 서버에서 받아오는 함수
    @Override
    public void run() {
        String message = null;

        try {
            while(true) {
                if (in != null) {
                    message = in.readLine();

                    if (message.contains("USERCOUNT:")) {
                        participantsButton.setText("참가자 : " + message.replace("USERCOUNT:", ""));
                    } else if (message.contains("USERNAME:")) {
                        message = message.replace("USERNAME:", "");
                        nameList = new ArrayList<>(Arrays.asList(message.split(" ")));
                    } else if (message.contains("KICKNAME:")) {
                        if (message.replace("KICKNAME:", "").equals(name)) {
                            in.close();
                            out.close();
                            socket.close();
                            break;
                        }
                    } else if (message.contains("MASTER:")) {
                        message = message.replace("MASTER:", "");
                        
                        if (message.equals(name)) {
                            master = true;
                            name = name + "(방장)";
                        }
                    } else {
                        chatTextArea.append(message + "\n");
                    }

                    int pos = chatTextArea.getText().length();
                    chatTextArea.setCaretPosition(pos);
                }
            }
        } catch(IOException e) {
            chatTextArea.append("서버 통신 오류...");
        }
    }

    //메세지를 서버로 전송하는 함수
    private void sendMessage() {
        String message = tf.getText();
                
        if (message.contains("USERCOUNT:") || message.contains("USERNAME:") || message.contains("KICKNAME:") || message.contains("MASTER:")) {
            chatTextArea.append("허용되지 않은 명령어입니다.\n");
            tf.setText("");
        } else {
            if (out != null) {
                out.println(message);
                out.flush();
                tf.setText("");
            }
        }
    }
}