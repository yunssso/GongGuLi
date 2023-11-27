package front.myPage;

import back.UserDTO;
import back.dao.CheckDAO;
import front.FrontSetting;
import front.ImagePanel;
import front.RoundedButton;
import front.RoundedButtonR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInfo {
    private FrontSetting fs = new FrontSetting();

    boolean checkNickDup;
    UserDTO userDTO = null;
    CheckDAO checkDAO = new CheckDAO();

    public UserInfo() {

    }

    public JPanel setUserInfoPanel() {  // 유저 정보 출력 패널
        ImagePanel userInfoPanel = new ImagePanel(new ImageIcon("img/roundedPanel.png").getImage());
        userInfoPanel.setBackground(Color.white);
        userInfoPanel.setBounds(25, 150, 210, 190);

        String[] userInfo = new String[5];

        userInfo[0] = "test";
        userInfo[1] = "test";
        userInfo[2] = "test";
        userInfo[3] = "test";
        userInfo[4] = "test";

        for (int i = 0; i < userInfo.length; i++) {
            int size = i;
            JLabel info = new JLabel(fs.userInfoHeader[i] + ": " + userInfo[i]);
            info.setFont(fs.fb14);
            info.setForeground(fs.c3);
            info.setBounds(20, 25 + (30 * size), 240, 20);
            userInfoPanel.add(info);
        }

        return userInfoPanel;
    }


    public void setModifyUserInfoFrame() {
        JFrame modifyUserInfoFrame = new JFrame("회원정보 수정");
        modifyUserInfoFrame.setSize(400, 500);
        fs.FrameSetting(modifyUserInfoFrame);
        modifyUserInfoFrame.setVisible(true);
        Container c = modifyUserInfoFrame.getContentPane();
        c.setBackground(fs.mainColor);
        c.setLayout(null);

        JLabel modifyUserInfoLabel = new JLabel("정보 수정");
        modifyUserInfoLabel.setForeground(fs.c3);
        modifyUserInfoLabel.setFont(fs.fb20);
        modifyUserInfoLabel.setBounds(158, 30, 100, 30);

        ImagePanel whitePanel = new ImagePanel(new ImageIcon("img/modifyUserInfoPanel.png").getImage());
        whitePanel.setBounds(25, 80, 330, 298);

        JLabel userNameLabel = new JLabel("이름");
        userNameLabel.setBounds(35, 20, 300, 30);
        userNameLabel.setFont(fs.f16);
        JLabel userName = new JLabel("test");
        userName.setBounds(160, 20, 300, 30);
        userName.setFont(fs.f16);

        JLabel userIDLabel = new JLabel("아이디");
        userIDLabel.setBounds(35, 52, 300, 30);
        userIDLabel.setFont(fs.f16);
        JLabel userID = new JLabel("test");
        userID.setBounds(160, 52, 300, 30);
        userID.setFont(fs.f16);

        JLabel userNickNameLabel = new JLabel("닉네임");
        userNickNameLabel.setBounds(35, 84, 70, 30);
        userNickNameLabel.setFont(fs.f16);

        JTextField userNickNameField = new JTextField("test");
        userNickNameField.setBounds(160, 84, 85, 30);

        RoundedButtonR NickDupBtn = new RoundedButtonR("확인");
        NickDupBtn.setBounds(250, 84, 48, 30);
        NickDupBtn.setFont(fs.f16);

        checkNickDup = false;
        NickDupBtn.addActionListener(new ActionListener() {  // 닉네임 중복 확인 버튼 클릭
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkDAO.nickNameCheck(userNickNameField.getText())) {
                    // 닉네임 변경 가능.
                    fs.showCompleteDialog("해당 닉네임은 사용 가능합니다.");
                    checkNickDup = true;
                } else fs.showErrorDialog("중복되는 닉네임이 존재합니다.");

                checkNickDup = true;
            }
        });

        JLabel userPWLabel = new JLabel("비밀번호");
        userPWLabel.setBounds(35, 116, 120, 30);
        userPWLabel.setFont(fs.f16);

        JPasswordField userPWField = new JPasswordField(20);
        userPWField.setBounds(160, 116, 140, 30);
        userPWField.setFont(fs.f16);

        JLabel userPWCheckLabel = new JLabel("비밀번호 확인");
        userPWCheckLabel.setBounds(35, 148, 150, 30);
        userPWCheckLabel.setFont(fs.f16);

        JPasswordField userPWCheckField = new JPasswordField(20);
        userPWCheckField.setBounds(160, 148, 140, 30);
        userPWCheckField.setFont(fs.f16);

        JLabel userRegionLabel = new JLabel("지역");
        userRegionLabel.setBounds(35, 180, 150, 30);
        userRegionLabel.setFont(fs.f16);

        JComboBox userRegionBtn = new JComboBox(fs.regionArr);
        userRegionBtn.setSelectedItem(userDTO.getRegion());
        userRegionBtn.setBounds(160, 180, 95, 30);
        userRegionBtn.setFont(fs.f16);

        JLabel userPhoneNumberLabel = new JLabel("휴대폰 번호");
        userPhoneNumberLabel.setBounds(35, 212, 300, 30);
        userPhoneNumberLabel.setFont(fs.f16);
        JLabel userPhoneNumber = new JLabel("test");
        userPhoneNumber.setBounds(160, 212, 300, 30);
        userPhoneNumber.setFont(fs.f16);

        JLabel userBirthLabel = new JLabel("생년월일");
        userBirthLabel.setBounds(35, 244, 300, 30);
        userBirthLabel.setFont(fs.f16);
        JLabel userBirth = new JLabel(userDTO.getBirth());
        userBirth.setBounds(160, 244, 300, 30);
        userBirth.setFont(fs.f16);

        RoundedButton modifyUserInfoBtn = new RoundedButton("수정하기");
        modifyUserInfoBtn.setBounds(143, 395, 100, 40);
        modifyUserInfoBtn.setFont(fs.fb16);

        modifyUserInfoBtn.addActionListener(new ActionListener() {  // 원래 본인 지역 먼저 뜨게 가능한지
            @Override
            public void actionPerformed(ActionEvent e) {
                // 닉네임 중복 확인 기능 필요
                char[] userPW = userPWField.getPassword();
                char[] userPWC = userPWCheckField.getPassword();
                String userPWStr = String.valueOf(userPW);

                boolean changeNick = false;
                boolean checkModify = false;

                if (!userNickNameField.getText().equals(userDTO.getNickName())) changeNick = true;

                if (userPWField.getText().isBlank() || userPWCheckField.getText().isBlank())
                    fs.showErrorDialog("비밀번호를 입력해주세요.");
                else if (!userPWField.getText().equals(userPWCheckField.getText()))
                    fs.showErrorDialog("비밀번호가 일치하지 않습니다.");
                else if (userPWStr.length() < 8 ||
                        !userPWStr.matches(".*[a-zA-Z].*") || // 영어 포함
                        !userPWStr.matches(".*\\d.*") ||      // 숫자 포함
                        !userPWStr.matches(".*[@#$%^&*+_=!].*")) { // 특수문자 포함
                    fs.showErrorDialog("비밀번호는 영어, 숫자, 특수문자를 포함하고 8글자 이상이어야 합니다.");
                } else if (userRegionBtn.getSelectedItem().equals(" --")) fs.showErrorDialog("지역을 선택해주세요.");
                else if (userNickNameField.getText().isBlank()) fs.showErrorDialog("닉네임을 입력해주세요.");
                else if (changeNick && !checkNickDup) fs.showErrorDialog("닉네임 중복확인을 해주세요.");
                else checkModify = true;

                if (checkModify) {
                    System.out.println("수정하기");
                    checkNickDup = false;
                    fs.showCompleteDialog("수정이 완료되었습니다.");
                    modifyUserInfoFrame.dispose();
                    // DB 비밀번호, 지역 수정
                }
            }
        });

        c.add(modifyUserInfoLabel);
        c.add(whitePanel);
        whitePanel.add(userNameLabel);
        whitePanel.add(userName);
        whitePanel.add(userIDLabel);
        whitePanel.add(userID);
        whitePanel.add(userNickNameLabel);
        whitePanel.add(userNickNameField);
        whitePanel.add(NickDupBtn);
        whitePanel.add(userPWLabel);
        whitePanel.add(userPWField);
        whitePanel.add(userPWCheckLabel);
        whitePanel.add(userPWCheckField);
        whitePanel.add(userRegionLabel);
        whitePanel.add(userRegionBtn);
        whitePanel.add(userPhoneNumberLabel);
        whitePanel.add(userPhoneNumber);
        whitePanel.add(userBirthLabel);
        whitePanel.add(userBirth);
        c.add(modifyUserInfoBtn);
    }
}
