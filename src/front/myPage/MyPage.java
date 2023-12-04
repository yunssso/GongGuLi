package front.myPage;

import back.dao.user.CheckDAO;
import back.UserDTO;
import back.request.account.DeleteUserRequest;
import back.response.ResponseCode;
import front.*;
import front.mainPage.MainPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MyPage extends JFrame {
    private UserInfo userInfo;
    String uuid;
    UserDTO userDTO = null;
    CheckDAO checkDAO = new CheckDAO();
    FrontSetting fs = new FrontSetting();

    public MyPage(String uuid) {  // 생성자
        this.uuid = uuid;
        this.userInfo = new UserInfo(uuid, this);

        setMyPage();
        setLeftPanel();
        setCenterPanel();
        setRightPanel();
    }

    public void setMyPage() {
        setSize(1120, 700);
        fs.FrameSetting(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("공구리 마이페이지");
        setLayout(null);

        setVisible(true);
    }

    private void setLeftPanel() {
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBackground(fs.mainColor);
        leftPanel.setBounds(0, 0, 300, 700);

        ImageIcon profileImgIcon = new ImageIcon("img/profile2.png");
        JButton profileBtn = new JButton(profileImgIcon);  // 유저 프로필 버튼
        profileBtn.setBounds(30, 30, 50, 50);
        profileBtn.setBorder(null);

        profileBtn.addActionListener(new ActionListener() {  // 프로필 버튼 누르면 작동될 기능
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainPage(uuid);
            }
        });

        JLabel nickNameLabel = new JLabel(userInfo.getnickName() + " 님");  // 닉네임 라벨
        nickNameLabel.setFont(fs.fb20);
        nickNameLabel.setForeground(fs.c3);
        nickNameLabel.setBounds(100, 30, 150, 50);

        RoundedButton modifyInfoBtn = new RoundedButton("정보 수정");
        modifyInfoBtn.setBounds(35, 350, 100, 30);
        modifyInfoBtn.setFont(fs.fb16);

        modifyInfoBtn.addActionListener(new ActionListener() {  // 정보 수정 버튼 클릭 시
            @Override
            public void actionPerformed(ActionEvent e) {
                userInfo.setModifyUserInfoFrame();
            }
        });

        JButton logoutBtn = new JButton("로그아웃");  // 로그아웃 버튼
        logoutBtn.setBounds(20, 590, 100, 20);
        logoutBtn.setBackground(null);
        logoutBtn.setBorder(null);
        logoutBtn.setFont(fs.f14);
        logoutBtn.setForeground(fs.c3);

        logoutBtn.addActionListener(new ActionListener() {  // 로그아웃 버튼 클릭
            @Override
            public void actionPerformed(ActionEvent e) {
                int answer = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?", "", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    System.out.println("로그아웃");
                    new LogIn();
                    dispose();
                } else System.out.println("노 로그아웃");

                // 로그인 화면으로
            }
        });

        JButton deleteIdBtn = new JButton("회원탈퇴");  // 회원탈퇴 버튼
        deleteIdBtn.setBounds(20, 615, 100, 20);
        deleteIdBtn.setBackground(null);
        deleteIdBtn.setBorder(null);
        deleteIdBtn.setFont(fs.f14);
        deleteIdBtn.setForeground(fs.c3);

        deleteIdBtn.addActionListener(new ActionListener() {  // 회원탈퇴 버튼 클릭
            @Override
            public void actionPerformed(ActionEvent e) {
                int answer = JOptionPane.showConfirmDialog(null, "회원탈퇴 하시겠습니까?", "", JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                    String password = JOptionPane.showInputDialog("비밀번호를 입력하세요.");
                    deleteUserMethod(password);
                }
                // 로그인 화면으로 + 회원 삭제
            }
        });

        add(leftPanel);
        leftPanel.add(profileBtn);
        leftPanel.add(nickNameLabel);
        leftPanel.add(modifyInfoBtn);
        leftPanel.add(logoutBtn);
        leftPanel.add(deleteIdBtn);
        leftPanel.add(userInfo.setUserInfoPanel());
    }

    private void setCenterPanel() {
        JPanel centerPanel = new JPanel(null);
        centerPanel.setBounds(300, 0, 770, 700);
        centerPanel.setBackground(Color.white);

        add(centerPanel);

        new MyPost(centerPanel, uuid, this);
        new MyHistory(centerPanel, uuid);
    }

    private void setRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(1070, 0, 50, 700);
        rightPanel.setBackground(fs.mainColor);

        add(rightPanel);
    }

    private void deleteUserMethod(String password) {
        try (Socket clientSocket = new Socket("43.200.49.16", 1024);
             OutputStream outputStream = clientSocket.getOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
             InputStream inputStream = clientSocket.getInputStream();
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ){
            DeleteUserRequest deleteUserRequest = new DeleteUserRequest(password, uuid);

            objectOutputStream.writeObject(deleteUserRequest);

            ResponseCode responseCode = (ResponseCode) objectInputStream.readObject();

            if (responseCode.getKey() == ResponseCode.DELETE_USER_SUCCESS.getKey()) { // 유저 삭제 성공
                new LogIn();
                dispose();
            } else { // 유저 삭제 실패

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}