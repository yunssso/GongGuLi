package front;

import back.ResponseCode;
import back.request.account.FindUserIdRequest;
import back.response.account.FindUserIdResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

class FindUserId extends JDialog {
    private JTextField nameText;
    private JTextField birthText;
    private JTextField phoneNumberText;
    private Font f1 = new Font("SUITE", Font.BOLD, 16);
    private Font f2 = new Font("SUITE", Font.BOLD, 9);
    private Color c1 = new Color(255, 240, 227);

    private Socket clientSocket = null;

    public FindUserId(JFrame parentFrame) {
        setTitle("아이디 찾기");
        setSize(400, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame); // 부모 프레임 중앙에 표시

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(c1);

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
        JButton findIdButton = new RoundedButton("아이디찾기");
        findIdButton.setBounds(30, 220, 150, 30);
        panel.add(findIdButton);
        findIdButton.setFont(f1);

        findIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameText.getText().trim();
                    String birth = birthText.getText().trim();
                    String phoneNumber = phoneNumberText.getText().trim();

                    //아이피, 포트 번호로 소켓을 연결
                    clientSocket = new Socket("localhost", 1024);

                    //서버로 정보를 전달 해주기 위해서 객체 형식으로 변환
                    FindUserIdRequest findUserIdRequest = new FindUserIdRequest(name, birth, phoneNumber);
                    
                    //서버와 정보를 주고 받기 위한 스트림 생성
                    OutputStream os = clientSocket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);

                    InputStream is = clientSocket.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);

                    oos.writeObject(findUserIdRequest);

                    ResponseCode responseCode = (ResponseCode) ois.readObject();

                    if (responseCode.getKey() == 230) { //아이디 찾기 성공
                        FindUserIdResponse findUserIdResponse = (FindUserIdResponse) ois.readObject();
                        showSuccessDialog(findUserIdResponse.userId());
                    } else { //아이디 찾기 실패
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
        add(panel);
        setVisible(true);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "알림", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "알림", JOptionPane.INFORMATION_MESSAGE);
    }
}
