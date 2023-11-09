package front;

import back.user.UserDAO;
import back.user.UserDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class FindPassword extends JDialog {
    UserDAO userDAO = new UserDAO();
    private JTextField nameText;
    private JTextField idText;
    private JTextField birthText;
    private JTextField phoneNumberText;
    private Font f1 = new Font("SUITE", Font.BOLD, 16);
    private Font f2 = new Font("SUITE", Font.BOLD, 9);

    public FindPassword(JFrame parentFrame) {
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
        JButton FindpwButton = new RoundedButton("비밀번호 찾기");
        FindpwButton.setBounds(30, 220, 150, 30);
        panel.add(FindpwButton);
        FindpwButton.setFont(f1);

        FindpwButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setName(nameText.getText());
                    userDTO.setUserId(idText.getText());
                    userDTO.setBirth(birthText.getText());
                    userDTO.setPhoneNum(phoneNumberText.getText());
                    if (userDAO.findPassword(userDTO)) {
                        String password = userDTO.getPassword();
                        System.out.println(password);
                    } else {
                        System.out.println("데이터가 존재하지 않습니다.");
                    }
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