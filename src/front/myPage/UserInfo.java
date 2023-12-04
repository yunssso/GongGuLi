package front.myPage;

import back.request.account.NickNameCheckRequest;
import back.response.ResponseCode;
import back.UserDTO;
import back.dao.user.CheckDAO;
import back.request.account.ModifyUserInfoRequest;
import back.request.mypage.UserInfoRequest;
import back.response.mypage.UserInfoResponse;
import front.FrontSetting;
import front.ImagePanel;
import front.RoundedButton;
import front.RoundedButtonR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class UserInfo {
    private final FrontSetting frontSetting = new FrontSetting();

    private final String uuid;
    boolean checkNickDup;
    private final String[] userInfo = new String[6];
    private JFrame myFrame;
    public UserInfo(String uuid, JFrame myFrame) {
        this.uuid = uuid;
        this.myFrame = myFrame;
        getUserInfo();
    }

    public JPanel setUserInfoPanel() {  // 유저 정보 출력 패널
        ImagePanel userInfoPanel = new ImagePanel(new ImageIcon("img/roundedPanel.png").getImage());
        userInfoPanel.setBackground(Color.white);
        userInfoPanel.setBounds(25, 150, 210, 190);

        for (int i = 1; i < userInfo.length; i++) {
            int size = i - 1;
            JLabel info = new JLabel(frontSetting.userInfoHeader[i - 1] + ": " + userInfo[i]);
            info.setFont(frontSetting.fb14);
            info.setForeground(frontSetting.c3);
            info.setBounds(20, 25 + (30 * size), 240, 20);
            userInfoPanel.add(info);
        }

        return userInfoPanel;
    }


    public void setModifyUserInfoFrame() {
        JFrame modifyUserInfoFrame = new JFrame("회원정보 수정");
        modifyUserInfoFrame.setSize(400, 500);
        frontSetting.FrameSetting(modifyUserInfoFrame);
        modifyUserInfoFrame.setVisible(true);
        Container c = modifyUserInfoFrame.getContentPane();
        c.setBackground(frontSetting.mainColor);
        c.setLayout(null);

        JLabel modifyUserInfoLabel = new JLabel("정보 수정");
        modifyUserInfoLabel.setForeground(frontSetting.c3);
        modifyUserInfoLabel.setFont(frontSetting.fb20);
        modifyUserInfoLabel.setBounds(158, 30, 100, 30);

        ImagePanel whitePanel = new ImagePanel(new ImageIcon("img/modifyUserInfoPanel.png").getImage());
        whitePanel.setBounds(25, 80, 330, 298);

        JLabel userNameLabel = new JLabel("이름");
        userNameLabel.setBounds(35, 20, 300, 30);
        userNameLabel.setFont(frontSetting.f16);
        JLabel userName = new JLabel(userInfo[1]);
        userName.setBounds(160, 20, 300, 30);
        userName.setFont(frontSetting.f16);

        JLabel userIDLabel = new JLabel("아이디");
        userIDLabel.setBounds(35, 52, 300, 30);
        userIDLabel.setFont(frontSetting.f16);
        JLabel userID = new JLabel(userInfo[2]);
        userID.setBounds(160, 52, 300, 30);
        userID.setFont(frontSetting.f16);

        JLabel userNickNameLabel = new JLabel("닉네임");
        userNickNameLabel.setBounds(35, 84, 70, 30);
        userNickNameLabel.setFont(frontSetting.f16);

        JTextField userNickNameField = new JTextField(userInfo[0]);
        userNickNameField.setBounds(160, 84, 85, 30);
        userNickNameField.setFont(frontSetting.f14);

        RoundedButtonR NickDupBtn = new RoundedButtonR("확인");
        NickDupBtn.setBounds(250, 84, 48, 30);
        NickDupBtn.setFont(frontSetting.f16);

        NickDupBtn.addActionListener(new ActionListener() {  // 닉네임 중복 확인 버튼 클릭
            @Override
            public void actionPerformed(ActionEvent e) {
                isCheckNickDup(userNickNameField.getText());
            }
        });

        JLabel userPWLabel = new JLabel("비밀번호");
        userPWLabel.setBounds(35, 116, 120, 30);
        userPWLabel.setFont(frontSetting.f16);

        JPasswordField userPWField = new JPasswordField(20);
        userPWField.setBounds(160, 116, 140, 30);

        JLabel userPWCheckLabel = new JLabel("비밀번호 확인");
        userPWCheckLabel.setBounds(35, 148, 150, 30);
        userPWCheckLabel.setFont(frontSetting.f16);

        JPasswordField userPWCheckField = new JPasswordField(20);
        userPWCheckField.setBounds(160, 148, 140, 30);

        JLabel userRegionLabel = new JLabel("지역");
        userRegionLabel.setBounds(35, 180, 150, 30);
        userRegionLabel.setFont(frontSetting.f16);

        JComboBox userRegionBtn = new JComboBox(frontSetting.regionArr);
        userRegionBtn.setSelectedItem(userInfo[3]);
        userRegionBtn.setBounds(160, 180, 95, 30);
        userRegionBtn.setFont(frontSetting.f16);


        JLabel userPhoneNumberLabel = new JLabel("휴대폰 번호");
        userPhoneNumberLabel.setBounds(35, 212, 300, 30);
        userPhoneNumberLabel.setFont(frontSetting.f16);
        JLabel userPhoneNumber = new JLabel(userInfo[4]);
        userPhoneNumber.setBounds(160, 212, 300, 30);
        userPhoneNumber.setFont(frontSetting.f16);

        JLabel userBirthLabel = new JLabel("생년월일");
        userBirthLabel.setBounds(35, 244, 300, 30);
        userBirthLabel.setFont(frontSetting.f16);
        JLabel userBirth = new JLabel(userInfo[5]);
        userBirth.setBounds(160, 244, 300, 30);
        userBirth.setFont(frontSetting.f16);

        RoundedButton modifyUserInfoBtn = new RoundedButton("수정하기");
        modifyUserInfoBtn.setBounds(143, 395, 100, 40);
        modifyUserInfoBtn.setFont(frontSetting.fb16);

        modifyUserInfoBtn.addActionListener(new ActionListener() {  // 원래 본인 지역 먼저 뜨게 가능한지
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickName = userNickNameField.getText();
                String password = String.valueOf(userPWField.getPassword());
                String passwordCheck = String.valueOf(userPWCheckField.getPassword());
                String region = (String) userRegionBtn.getSelectedItem();

                if (userNickNameField.getText().equals(userInfo[0])) checkNickDup = true;

                if (checkNickDup) {
                    if (modifyUserInfoMethod(nickName, password, passwordCheck, region)) {
                        checkNickDup = false;
                        frontSetting.showCompleteDialog("수정이 완료되었습니다.");
                        myFrame.dispose();
                        new MyPage(uuid);
                        modifyUserInfoFrame.dispose();

                    }
                } else {
                    frontSetting.showErrorDialog("닉네임 중복확인을 해주세요.");
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

    private void isCheckNickDup(String inpNickName) {
        try (Socket clientSocket = new Socket("localhost", 1024);
             OutputStream outputStream = clientSocket.getOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
             InputStream inputStream = clientSocket.getInputStream();
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ) {
            NickNameCheckRequest nickNameCheckRequest = new NickNameCheckRequest(uuid, inpNickName);

            objectOutputStream.writeObject(nickNameCheckRequest);

            ResponseCode responseCode = (ResponseCode) objectInputStream.readObject();

            if (responseCode.getKey() == ResponseCode.NICKNAME_CHECK_SUCCESS.getKey()) {
                frontSetting.showCompleteDialog(responseCode.getValue());
                checkNickDup = true;
            } else if (responseCode.getKey() == ResponseCode.NICKNAME_CHECK_FAILURE.getKey()) {
                frontSetting.showErrorDialog(responseCode.getValue());
                checkNickDup = false;
            } else if (responseCode.getKey() == ResponseCode.NICKNAME_CHECK_MY_NAME.getKey()) {
                frontSetting.showErrorDialog(responseCode.getValue());
                checkNickDup = true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /*writer, name, userId, region, phoneNum, birth를 서버에서 받아오는 메소드*/
    private void getUserInfo() {
        try (Socket clientSocket = new Socket("localhost", 1028);
             OutputStream outputStream = clientSocket.getOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
             InputStream inputStream = clientSocket.getInputStream();
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ) {
            UserInfoRequest userInfoRequest = new UserInfoRequest(uuid); // uuid를 Request 객체로 만듦

            objectOutputStream.writeObject(userInfoRequest); // uuid를 서버에 보내서 유저정보 요청

            ResponseCode responseCode = (ResponseCode) objectInputStream.readObject(); // 서버에서 응답 코드를 받아옴

            if (responseCode.getKey() == ResponseCode.GET_USERINFO_SUCCESS.getKey()) { // 유저정보 갱신 성공
                UserInfoResponse userInfoResponse = (UserInfoResponse) objectInputStream.readObject(); // 서버에서 유저정보를 받아옴

                userInfo[0] = userInfoResponse.nickName();
                userInfo[1] = userInfoResponse.name();
                userInfo[2] = userInfoResponse.userId();
                userInfo[3] = userInfoResponse.region();
                userInfo[4] = userInfoResponse.phoneNum();
                userInfo[5] = userInfoResponse.birth();
            } else { // 유저정보 갱신 실패
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /*유저정보 수정 Request 메소드*/
    private boolean modifyUserInfoMethod(String nickName, String password, String passwordCheck, String region) {
        try (Socket clientSocket = new Socket("localhost", 1024);
             OutputStream outputStream = clientSocket.getOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
             InputStream inputStream = clientSocket.getInputStream();
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ){
            ModifyUserInfoRequest modifyUserInfoRequest = new ModifyUserInfoRequest(nickName, password, passwordCheck, region, uuid);

            objectOutputStream.writeObject(modifyUserInfoRequest);

            ResponseCode responseCode = (ResponseCode) objectInputStream.readObject();

            if (responseCode.getKey() == ResponseCode.MODIFY_USER_INFO_SUCCESS.getKey()) { // 유저 정보 수정 성공
                return true;
            } else { // 유저 정보 수정 실패
                frontSetting.showErrorDialog(responseCode.getValue());

                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public String getnickName() {
        return userInfo[0];
    }
}
