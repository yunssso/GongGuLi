import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame {

    private Color c1 = new Color(255, 240, 227);
    private Color c3 = new Color(255, 255, 255);

    private Font f1 = new Font("SUITE", Font.BOLD, 16);
    private Font f2 = new Font("SUITE", Font.BOLD, 9);

    private JTextField idText;
    private JPasswordField passwordText;

    public Login() {
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
    }

    private void setLeftPanel() {
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(0, 0, 560, 700);
        leftPanel.setBackground(c1);

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\user\\Desktop\\찐공구리.png");
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
        JButton loginButton = new RoundedButtonS("로그인");
        loginButton.setBounds(910, 365, 100, 70);
        panel.add(loginButton);
        loginButton.setFont(f1);

        // 회원가입 버튼
        JButton registerButton = new RoundedButtonS("회원가입");
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
                if (validateInput()) {
                    String id = idText.getText();
                    char[] password = passwordText.getPassword();
                    System.out.println("아이디: " + id);
                    System.out.println("비밀번호: " + new String(password));
                }
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
                new FindUsernameDialog(Login.this);
            }
        });

        // 비밀번호 찾기 버튼 클릭 시
        pwSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 비밀번호 찾기 창을 띄우기
                new FindPasswordDialog(Login.this);
            }
        });
    }

    private boolean validateInput() {
        String username = idText.getText().trim();
        char[] password = passwordText.getPassword();

        if (username.isBlank()) {
            showErrorDialog("아이디를 입력해 주세요.");
            return false;
        }

        if (String.valueOf(password).isBlank()) {
            showErrorDialog("비밀번호를 입력해 주세요.");
            return false;
        }

        return true;
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "입력 오류", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
}

class FindUsernameDialog extends JDialog {
    private JTextField nameText;
    private JTextField birthText;
    private JTextField phoneNumberText;
    private Font f1 = new Font("SUITE", Font.BOLD, 16);
    private Font f2 = new Font("SUITE", Font.BOLD, 9);

    public FindUsernameDialog(JFrame parentFrame) {
        setTitle("아이디 찾기");
        setSize(400, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame); // 부모 프레임 중앙에 표시

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // 이름 입력 필드
        JLabel nameLabel = new JLabel("이름:");
        nameLabel.setBounds(30, 50, 100, 30);
        panel.add(nameLabel);
        nameLabel.setFont(f1);

        nameText = new JTextField(20);
        nameText.setBounds(150, 50, 200, 30);
        panel.add(nameText);
        nameText.setFont(f2);

        // 생년월일 입력 필드
        JLabel birthLabel = new JLabel("생년월일:");
        birthLabel.setBounds(30, 100, 100, 30);
        panel.add(birthLabel);
        birthLabel.setFont(f1);

        birthText = new JTextField(20);
        birthText.setBounds(150, 100, 200, 30);
        panel.add(birthText);
        birthText.setFont(f2);

        // 핸드폰 번호 입력 필드
        JLabel phoneNumberLabel = new JLabel("핸드폰 번호:");
        phoneNumberLabel.setBounds(30, 150, 100, 30);
        panel.add(phoneNumberLabel);
        phoneNumberLabel.setFont(f1);

        phoneNumberText = new JTextField(20);
        phoneNumberText.setBounds(150, 150, 200, 30);
        panel.add(phoneNumberText);
        phoneNumberText.setFont(f2);

        // 아이디 찾기 버튼
        JButton findIdButton = new RoundedButtonS("아이디찾기");
        findIdButton.setBounds(30, 220, 150, 30);
        panel.add(findIdButton);
        findIdButton.setFont(f1);

        findIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    String name = nameText.getText();
                    String birth = birthText.getText();
                    String phoneNumber = phoneNumberText.getText();
                }
            }
        });
        add(panel);
        setVisible(true);
    }
    private boolean validateInput() {
        String name = nameText.getText().trim();
        String birth = birthText.getText().trim();
        String phoneNumber = phoneNumberText.getText().trim();

        if (name.isEmpty() || birth.isEmpty() || phoneNumber.isEmpty()) {
            showErrorDialog("모든 항목을 입력하세요.");
            return false;
        }

        return true;
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "입력 오류", JOptionPane.ERROR_MESSAGE);
    }
}
class FindPasswordDialog extends JDialog {
    private JTextField nameText;
    private JTextField idText;
    private JTextField birthText;
    private JTextField phoneNumberText;
    private Font f1 = new Font("SUITE", Font.BOLD, 16);
    private Font f2 = new Font("SUITE", Font.BOLD, 9);

    public FindPasswordDialog(JFrame parentFrame) {
        setTitle("비밀번호 찾기");
        setSize(400, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame); // 부모 프레임 중앙에 표시

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // 이름 입력 필드
        JLabel nameLabel = new JLabel("이름:");
        nameLabel.setBounds(30, 20, 100, 30);
        panel.add(nameLabel);
        nameLabel.setFont(f1);

        nameText = new JTextField(20);
        nameText.setBounds(150, 20, 200, 30);
        panel.add(nameText);
        nameText.setFont(f2);

        // 아이디 입력 필드
        JLabel idLabel = new JLabel("아이디:");
        idLabel.setBounds(30, 70, 100, 30);
        panel.add(idLabel);
        idLabel.setFont(f1);

        idText = new JTextField(20);
        idText.setBounds(150, 70, 200, 30);
        panel.add(idText);
        idText.setFont(f2);

        // 생년월일 입력 필드
        JLabel birthLabel = new JLabel("생년월일:");
        birthLabel.setBounds(30, 120, 100, 30);
        panel.add(birthLabel);
        birthLabel.setFont(f1);

        birthText = new JTextField(20);
        birthText.setBounds(150, 120, 200, 30);
        panel.add(birthText);
        birthText.setFont(f2);

        // 핸드폰 번호 입력 필드
        JLabel phoneNumberLabel = new JLabel("핸드폰 번호:");
        phoneNumberLabel.setBounds(30, 170, 100, 30);
        panel.add(phoneNumberLabel);
        phoneNumberLabel.setFont(f1);

        phoneNumberText = new JTextField(20);
        phoneNumberText.setBounds(150, 170, 200, 30);
        panel.add(phoneNumberText);
        phoneNumberText.setFont(f2);

        //비밀번호 찾기 버튼
        JButton FindpwButton = new RoundedButtonS("비밀번호 찾기");
        FindpwButton.setBounds(30, 220, 150, 30);
        panel.add(FindpwButton);
        FindpwButton.setFont(f1);

        FindpwButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    String name = nameText.getText();
                    String birth = birthText.getText();
                    String phoneNumber = phoneNumberText.getText();
                }
            }
        });

        add(panel);
        setVisible(true);
    }
    private boolean validateInput() {
        String name = nameText.getText().trim();
        String id = idText.getText().trim();
        String birth = birthText.getText().trim();
        String phoneNumber = phoneNumberText.getText().trim();

        if (name.isEmpty() || id.isEmpty() || birth.isEmpty() || phoneNumber.isEmpty()) {
            showErrorDialog("모든 항목을 입력하세요.");
            return false;
        }
        return true;
    }
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "입력 오류", JOptionPane.ERROR_MESSAGE);
    }
}
