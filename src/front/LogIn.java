package front;

import back.ResponseCode;
import back.request.account.LoginRequest;
import back.response.account.LoginResponse;
import front.mainPage.MainPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class LogIn extends JFrame {
    FrontSetting fs = new FrontSetting();

    private Color c1 = new Color(255, 240, 227);
    private Color c3 = new Color(255, 255, 255);

    private Font f1 = new Font("SUITE", Font.BOLD, 16);
    private Font f2 = new Font("SUITE", Font.BOLD, 9);

    private JTextField idText;
    private JPasswordField passwordText;

    private Socket clientSocket = null;

    public LogIn() {
        setLoginFrame();
        setLeftPanel();
        setRightPanel();
        setCenterPanel();
        setVisible(true);
    }

    private void setLoginFrame() {
        setTitle("로그인");
        setSize(1120, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fs.FrameSetting(this);
    }

    private void setLeftPanel() {
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(0, 0, 560, 700);
        leftPanel.setBackground(c1);

        ImageIcon imageIcon = new ImageIcon("img/logo.png");
        Image image = imageIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(image);

        JLabel imageLabel = new JLabel(scaledImageIcon);
        imageLabel.setBounds(25, 10, 300, 300);
        leftPanel.add(imageLabel);
        add(leftPanel);
    }

    private void setRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(1070, 0, 50, 700);
        rightPanel.setBackground(c1);

        add(rightPanel);
    }

    public void setCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(c3);
        add(panel);

        // 아이디 입력 필드
        JLabel idLabel = new JLabel("아이디:");
        idLabel.setBounds(600, 365, 100, 30);
        panel.add(idLabel);
        idLabel.setFont(f1);

        idText = new JTextField(20);
        idText.setBounds(700, 365, 200, 30);
        panel.add(idText);
        idText.setFont(f2);

        // 비밀번호 입력 필드
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordLabel.setBounds(600, 405, 100, 30);
        panel.add(passwordLabel);
        passwordLabel.setFont(f1);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(700, 405, 200, 30);
        panel.add(passwordText);
        passwordText.setFont(f2);

        // 로그인 버튼
        JButton loginButton = new RoundedButton("로그인");
        loginButton.setBounds(910, 365, 100, 70);
        panel.add(loginButton);
        loginButton.setFont(f1);

        // 회원가입 버튼
        JButton registerButton = new RoundedButton("회원가입");
        registerButton.setBounds(700, 445, 200, 30);
        panel.add(registerButton);
        registerButton.setFont(f1);

        // 아이디 찾기 버튼
        JButton idSearchButton = new JButton("아이디 찾기");
        idSearchButton.setBounds(700, 485, 100, 30);
        idSearchButton.setFont(f2);
        idSearchButton.setOpaque(false);
        idSearchButton.setContentAreaFilled(false);
        idSearchButton.setBorderPainted(false);
        panel.add(idSearchButton);

        JLabel verticalLineLabel = new JLabel("|");
        panel.add(verticalLineLabel);
        verticalLineLabel.setBounds(799, 484, 100, 30);

        // 비밀번호 찾기 버튼
        JButton pwSearchButton = new JButton("비밀번호 찾기");
        pwSearchButton.setBounds(800, 485, 100, 30);
        pwSearchButton.setFont(f2);
        pwSearchButton.setOpaque(false);
        pwSearchButton.setContentAreaFilled(false);
        pwSearchButton.setBorderPainted(false);
        panel.add(pwSearchButton);

        // 로그인 버튼 클릭 시
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = idText.getText();
                    String password = String.valueOf(passwordText.getPassword());

                    //아이피, 포트 번호로 소켓을 연결
                    clientSocket = new Socket("localhost", 1024);

                    //서버로 정보를 전달 해주기 위해서 객체 형식으로 변환
                    LoginRequest loginRequest = new LoginRequest(id, password);

                    //서버와 정보를 주고 받기 위한 스트림 생성
                    OutputStream os = clientSocket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);

                    InputStream is = clientSocket.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);

                    oos.writeObject(loginRequest);

                    ResponseCode responseCode = (ResponseCode) ois.readObject();

                    if (responseCode.getKey() == 220) { //로그인 성공
                        LoginResponse loginResponse = (LoginResponse) ois.readObject();
                        String uuid = loginResponse.uuid();

                        dispose();
                        new MainPage(uuid); //uuid 받아 오는거 구현 해놨으니까 이거 가져다가 쓰면 돼.
                    } else { //로그인 실패
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
        idText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                loginButton.doClick();
            }
        });

        // 회원가입 버튼 클릭 시
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SignUp();
            }
        });

        // 아이디 찾기 버튼 클릭 시
        idSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 아이디 찾기 창을 띄우기
                new FindUserId(LogIn.this);
            }
        });

        // 비밀번호 찾기 버튼 클릭 시
        pwSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 비밀번호 찾기 창을 띄우기
                new FindPassword(LogIn.this);
            }
        });
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "입력 오류", JOptionPane.ERROR_MESSAGE);
    }
}