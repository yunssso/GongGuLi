package front;

import back.request.account.SignUpRequest;
import back.ResponseCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.io.*;

public class SignUp extends JDialog{
    private Color c1 = new Color(255, 240, 227);
    private Color c3 = new Color(255, 255, 255);

    private Font f1 = new Font("SUITE", Font.BOLD, 16);
    private Font f2 = new Font("SUITE", Font.BOLD, 14);
    
    private JTextField idText;
    private JPasswordField passwordText;
    private JPasswordField pwCheckText;
    private JTextField nameText;
    private JTextField birthText;
    private JTextField phoneNumberText;
    private JTextField nickNameText;
    private JComboBox<String> residenceList;

    private boolean membershipProgress = false;

    private Socket clientSocket = null;

    public SignUp() {
        setSignUp();
        setLeftPanel();
        setRightPanel();
        setLocationRelativeTo(null);
    }

    private void setLeftPanel() {
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(0, 0, 560, 700);
        leftPanel.setBackground(c1);

        // BackButton을 leftPanel 위에 추가
        setBackButton();

        add(leftPanel);
    }

    private void setRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(1070, 0, 50, 700);
        rightPanel.setBackground(c1);

        add(rightPanel);
    }

    public void setSignUp() {
        setTitle("회원가입");
        setSize(1120, 700);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(c3);
        setContentPane(panel);

        // 아이디 입력 필드
        JLabel idLabel = new JLabel("아이디");
        idLabel.setBounds(635, 195, 100, 30);
        idLabel.setFont(f1);
        idLabel.setHorizontalAlignment(JLabel.RIGHT);
        idText = new JTextField(20);
        idText.setBounds(758, 195, 200, 30);
        idText.setFont(f2);

        panel.add(idLabel);
        panel.add(idText);


        // 비밀번호 입력 필드
        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setBounds(635, 235, 100, 30);
        passwordLabel.setFont(f1);
        passwordText = new JPasswordField(20);
        passwordText.setBounds(758, 235, 200, 30);
        passwordLabel.setHorizontalAlignment(JLabel.RIGHT);

        panel.add(passwordLabel);
        panel.add(passwordText);

        // 비밀번호 확인 입력 필드
        JLabel pwCheckLabel = new JLabel("비밀번호 확인");
        pwCheckLabel.setBounds(635, 275, 100, 30);
        pwCheckLabel.setHorizontalAlignment(JLabel.RIGHT);
        pwCheckLabel.setFont(f1);
        pwCheckText = new JPasswordField(20);
        pwCheckText.setBounds(758, 275, 200, 30);

        panel.add(pwCheckLabel);
        panel.add(pwCheckText);

        // 이름 입력 필드
        JLabel nameLabel = new JLabel("이름");
        nameLabel.setBounds(635, 315, 100, 30);
        nameLabel.setFont(f1);
        nameLabel.setHorizontalAlignment(JLabel.RIGHT);
        nameText = new JTextField(20);
        nameText.setBounds(758, 315, 200, 30);

        nameText.setFont(f2);
        panel.add(nameLabel);
        panel.add(nameText);

        // 닉네임 입력 필드
        JLabel nickNameLabel = new JLabel("닉네임");
        nickNameLabel.setBounds(635, 355, 100, 30);
        nickNameLabel.setHorizontalAlignment(JLabel.RIGHT);
        nickNameLabel.setFont(f1);
        nickNameText = new JTextField(20);
        nickNameText.setBounds(758, 355, 200, 30);
        nickNameText.setFont(f2);

        panel.add(nickNameLabel);
        panel.add(nickNameText);


        // 생일 입력 필드
        JLabel birthLabel = new JLabel("생년월일");
        birthLabel.setBounds(635, 395, 100, 30);
        birthLabel.setHorizontalAlignment(JLabel.RIGHT);
        birthLabel.setFont(f1);
        birthText = new JTextField(20);
        birthText.setBounds(758, 395, 200, 30);
        birthText.setFont(f2);
        panel.add(birthLabel);
        panel.add(birthText);


        // 폰 번호 입력 필드
        JLabel phoneNumberLabel = new JLabel("핸드폰 번호");
        phoneNumberLabel.setBounds(635, 435, 100, 30);
        phoneNumberLabel.setHorizontalAlignment(JLabel.RIGHT);
        phoneNumberLabel.setFont(f1);
        phoneNumberText = new JTextField(20);
        phoneNumberText.setBounds(758, 435, 200, 30);
        phoneNumberText.setFont(f2);

        panel.add(phoneNumberLabel);
        panel.add(phoneNumberText);


        // 거주 지역 입력 컴포넌트
        JLabel residenceLabel = new JLabel("거주 지역");
        residenceLabel.setBounds(635, 475, 100, 30);
        residenceLabel.setHorizontalAlignment(JLabel.RIGHT);
        residenceLabel.setFont(f1);
        residenceList = new JComboBox<>(new String[]{"서울", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주도"});
        residenceList.setBounds(758, 475, 200, 30);
        residenceList.setFont(f2);

        panel.add(residenceLabel);
        panel.add(residenceList);

        // 회원가입 버튼
        JButton signUpButton = new RoundedButton("회원가입");
        signUpButton.setBounds(758, 535, 200, 30);
        signUpButton.setFont(f1);
        panel.add(signUpButton);


        // 회원가입 버튼 클릭 시
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String userId = idText.getText();
                    String password = String.valueOf(passwordText.getPassword()) ;
                    String passwordCheck = String.valueOf(pwCheckText.getPassword());
                    String name = nameText.getText();
                    String birth = birthText.getText();
                    String phoneNum = phoneNumberText.getText();
                    String nickName = nickNameText.getText();
                    String region = (String) residenceList.getSelectedItem();

                    //서버로 정보를 전달 해주기 위해서 객체 형식으로 변환
                    SignUpRequest signUpDto = new SignUpRequest(userId, password, passwordCheck, name, birth, phoneNum, nickName, region);

                    //아이피, 포트 번호로 소켓을 연결
                    clientSocket = new Socket("localhost", 1024);

                    //서버와 정보를 주고 받기 위한 스트림 생성
                    OutputStream os = clientSocket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);

                    InputStream is = clientSocket.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);

                    oos.writeObject(signUpDto);

                    ResponseCode responseCode = (ResponseCode) ois.readObject();

                    if (responseCode.getKey() == ResponseCode.SIGNUP_SUCCESS.getKey()) { //회원가입 성공
                        showSuccessDialog(responseCode.getValue());
                        dispose();
                        new LogIn();
                    } else { //회원가입 실패
                        showErrorDialog(responseCode.getValue());
                    }

                    oos.close();
                    os.close();

                    ois.close();
                    is.close();

                    clientSocket.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        setVisible(true);
    }

    private void setBackButton() {
        // 이미지 아이콘 생성
        ImageIcon backButtonIcon = new ImageIcon("img/뒤로가기버튼.png");

        //원본 이미지 가져오기
        JLabel backButtonLabel = new JLabel(backButtonIcon);

        // 이미지 크기 조절
        int newWidth = 50; // 새로운 너비
        int newHeight = 50; // 새로운 높이
        Image image = backButtonIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        ImageIcon resizedIcon = new ImageIcon(image);
        backButtonLabel.setIcon(resizedIcon);

        // 라벨의 위치와 크기 설정
        backButtonLabel.setBounds(50, 40, newWidth, newHeight);

        // 프레임에 라벨 추가
        add(backButtonLabel);
        // 라벨에 마우스 클릭 이벤트 추가
        backButtonLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new LogIn();
            }
        });
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "안내", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "알림", JOptionPane.INFORMATION_MESSAGE);
    }
}