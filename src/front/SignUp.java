package front;

import back.UserDAO;
import back.UserDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignUp extends JDialog {
    private Color c1 = new Color(255, 240, 227);
    private Color c3 = new Color(255, 255, 255);

    private Font f1 = new Font("SUITE", Font.BOLD, 16);
    private Font f2 = new Font("SUITE", Font.BOLD, 10);

    private JTextField idText;
    private JPasswordField passwordText;
    private JPasswordField pwCheckText;
    private JTextField nameText;
    private JTextField birthText;
    private JTextField phoneNumberText;
    private JTextField nickNameText;
    private JComboBox<String> residenceList;

    private boolean membershipProgress = false;

    public SignUp() {
        setSignUp();
        setLeftPanel();
        setRightPanel();
    }

    private void setLeftPanel() {
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(0, 0, 300, 700);
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
        JLabel idLabel = new JLabel("아이디:");
        idLabel.setBounds(538, 195, 100, 30);
        idText = new JTextField(20);
        idText.setBounds(638, 195, 200, 30);

        panel.add(idLabel);
        idLabel.setFont(f1);
        panel.add(idText);
        idText.setFont(f2);

        // 비밀번호 입력 필드
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordLabel.setBounds(521, 235, 100, 30);
        passwordText = new JPasswordField(20);
        passwordText.setBounds(638, 235, 200, 30);
        panel.add(passwordLabel);
        passwordLabel.setFont(f1);
        panel.add(passwordText);
        passwordText.setFont(f2);

        // 비밀번호 확인 입력 필드
        JLabel pwCheckLabel = new JLabel("비밀번호 확인:");
        pwCheckLabel.setBounds(486, 275, 150, 30);
        pwCheckText = new JPasswordField(20);
        pwCheckText.setBounds(638, 275, 200, 30);
        panel.add(pwCheckLabel);
        pwCheckLabel.setFont(f1);
        panel.add(pwCheckText);
        pwCheckText.setFont(f2);

        // 이름 입력 필드
        JLabel nameLabel = new JLabel("이름:");
        nameLabel.setBounds(556, 315, 100, 30);
        nameText = new JTextField(20);
        nameText.setBounds(638, 315, 200, 30);
        panel.add(nameLabel);
        nameLabel.setFont(f1);
        panel.add(nameText);
        nameText.setFont(f2);

        // 생일 입력 필드
        JLabel birthLabel = new JLabel("생년월일:");
        birthLabel.setBounds(525, 395, 100, 30);
        birthText = new JTextField(20);
        birthText.setBounds(638, 395, 200, 30);
        panel.add(birthLabel);
        birthLabel.setFont(f1);
        panel.add(birthText);
        birthText.setFont(f2);

        // 폰 번호 입력 필드
        JLabel phoneNumberLabel = new JLabel("핸드폰 번호:");
        phoneNumberLabel.setBounds(505, 435, 100, 30);
        phoneNumberText = new JTextField(20);
        phoneNumberText.setBounds(638, 435, 200, 30);
        panel.add(phoneNumberLabel);
        phoneNumberLabel.setFont(f1);
        panel.add(phoneNumberText);
        phoneNumberText.setFont(f2);

        // 닉네임 입력 필드
        JLabel nickNameLabel = new JLabel("닉네임:");
        nickNameLabel.setBounds(538, 355, 100, 30);
        nickNameText = new JTextField(20);
        nickNameText.setBounds(638, 355, 200, 30);
        panel.add(nickNameLabel);
        nickNameLabel.setFont(f1);
        panel.add(nickNameText);
        nickNameText.setFont(f2);

        // 거주 지역 입력 컴포넌트
        JLabel residenceLabel = new JLabel("거주 지역:");
        residenceLabel.setBounds(520, 475, 100, 30);
        residenceList = new JComboBox<>(new String[]{"서울", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주도"});
        residenceList.setBounds(638, 475, 200, 30);
        panel.add(residenceLabel);
        residenceLabel.setFont(f1);
        panel.add(residenceList);

        // 회원가입 버튼
        JButton signUpButton = new RoundedButton("회원가입");
        signUpButton.setBounds(638, 535, 200, 30);
        panel.add(signUpButton);
        signUpButton.setFont(f1);

        // 회원가입 버튼 클릭 시
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    String username = idText.getText();
                    char[] password = passwordText.getPassword();
                    char[] passwordCheck = pwCheckText.getPassword();
                    String name = nameText.getText();
                    String birth = birthText.getText();
                    String phoneNumber = phoneNumberText.getText();
                    String nickName = nickNameText.getText();
                    String residence = (String) residenceList.getSelectedItem();

                    System.out.println("아이디: " + username);
                    System.out.println("비밀번호: " + new String(password));
                    System.out.println("비밀번호 확인: " + new String(passwordCheck));
                    System.out.println("이름: " + name);
                    System.out.println("닉네임: " + nickName);
                    System.out.println("생년월일: " + birth);
                    System.out.println("휴대폰 번호: " + phoneNumber);
                    System.out.println("거주 지역: " + residence);

                    UserDTO userDTO = new UserDTO(nickName, name, username, new String(password), residence, phoneNumber, birth);
                    UserDAO userDAO = new UserDAO();
                    userDAO.signUp(userDTO);
                    new LogIn();
                }
            }
        });
        setVisible(true);
    }

    private void setBackButton() {
        // 이미지 아이콘 생성
        ImageIcon backButtonIcon = new ImageIcon("C:\\Users\\user\\Desktop\\뒤로가기.png");

        //원본 이미지 가져오기
        JLabel backButtonLabel = new JLabel(backButtonIcon);

        // 이미지 크기 조절
        int newWidth = 50; // 새로운 너비
        int newHeight = 50; // 새로운 높이
        Image image = backButtonIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        ImageIcon resizedIcon = new ImageIcon(image);
        backButtonLabel.setIcon(resizedIcon);

        // 라벨의 위치와 크기 설정
        backButtonLabel.setBounds(150, 50, newWidth, newHeight);

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

    private boolean validateInput() {
        String username = idText.getText().trim();
        char[] password = passwordText.getPassword();
        char[] passwordCheck = pwCheckText.getPassword();
        String name = nameText.getText().trim();
        String birth = birthText.getText().trim();
        String phoneNumber = phoneNumberText.getText().trim();
        String nickName = nickNameText.getText().trim();
        String residence = (String) residenceList.getSelectedItem();

        if (username.isBlank()) {
            showErrorDialog("아이디를 입력해 주세요.");
            return false;
        }

        if (String.valueOf(password).isBlank()) {
            showErrorDialog("비밀번호를 입력해 주세요.");
            return false;
        }

        if (!String.valueOf(password).equals(String.valueOf(passwordCheck))) {
            showErrorDialog("비밀번호가 일치하지 않습니다.");
            return false;
        }
        String passwordStr = String.valueOf(password);
        if (passwordStr.isBlank() || passwordStr.length() < 8 ||
                !passwordStr.matches(".*[a-zA-Z].*") || // 영어 포함
                !passwordStr.matches(".*\\d.*") ||      // 숫자 포함
                !passwordStr.matches(".*[@#$%^&*+_=!].*")) { // 특수문자 포함
            showErrorDialog("비밀번호는 영어, 숫자, 특수문자를 포함하고 8글자 이상이어야 합니다.");
            return false;
        }

        if (name.isBlank()) {
            showErrorDialog("이름을 입력해 주세요.");
            return false;
        }

        if (birth.isBlank()) {
            showErrorDialog("생년월일을 입력해 주세요.");
            return false;
        }
        // 생년월일 유효성 검사
        if (!birth.matches("\\d{6}")) { // 6글자 숫자인지 확인
            showErrorDialog("생년월일은 6글자의 숫자로 입력해 주세요.");
            return false;
        }

        if (phoneNumber.isBlank()) {
            showErrorDialog("휴대폰 번호를 입력해 주세요.");
            return false;
        }

        if (!phoneNumber.matches("\\d{11}")) { // 6글자 숫자인지 확인
            showErrorDialog("휴대폰 번호는 11글자의 숫자로 입력해 주세요.");
            return false;
        }

        if (nickName.isBlank()) {
            showErrorDialog("닉네임을 입력해 주세요.");
            return false;
        }

        if (residence.equals("거주 지역")) {
            showErrorDialog("거주지역을 선택해 주세요.");
            return false;
        }

        return true;
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "입력 오류", JOptionPane.ERROR_MESSAGE);
    }
}