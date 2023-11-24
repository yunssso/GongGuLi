package front.MainPage;

import back.UserDTO;
import front.FrontSetting;
import front.MyPage;
import front.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModifyMyPost extends MainPage{
    FrontSetting frontSetting = new FrontSetting();
    public ModifyMyPost() {
        modifyMyPost();
    }
    private void modifyMyPost() {  // 글 수정하기
        JFrame modifyFrame = new JFrame("수정하기");   // 수정 프레임
        modifyFrame.setSize(500, 600);
        frontSetting.FrameSetting(modifyFrame);

        Container c = modifyFrame.getContentPane();  // 수정 컨테이너
        c.setBackground(frontSetting.mainColor);
        c.setLayout(null);

        JLabel newPostLabel = new JLabel("수정하기");  // 라벨
        newPostLabel.setFont(frontSetting.fb20);
        newPostLabel.setBounds(210, 20, 100, 40);

        JLabel titleLabel = new JLabel("  제목:");  // 제목 라벨
        titleLabel.setBounds(20, 80, 80, 35);
        titleLabel.setFont(frontSetting.f18);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.WHITE);

        JTextField titleField = new JTextField();  // 제목 입력 필드
        titleField.setBounds(80, 80, 385, 35);
        titleField.setFont(frontSetting.f18);
        titleField.setBorder(null);

        JPanel whitePanel = new JPanel(null);  // 지역, 카테고리의 흰색 배경
        whitePanel.setBounds(20, 125, 445, 35);
        whitePanel.setBackground(Color.WHITE);

        JLabel regionLabel = new JLabel(" 지역: ");  // 지역 라벨
        regionLabel.setBounds(5, 3, 70, 30);
        regionLabel.setFont(frontSetting.f18);

        JComboBox regionField = new JComboBox(frontSetting.regionArr);  // 지역 콤보 박스
        regionField.setBounds(60, 3, 100, 30);
        regionField.setFont(frontSetting.f16);

        JLabel categoryLabel = new JLabel("카테고리: ");  // 카테고리 라벨
        categoryLabel.setBounds(170, 3, 100, 30);
        categoryLabel.setFont(frontSetting.f18);

        JComboBox categoryField = new JComboBox(frontSetting.categoryArr);  // 카테고리 콤보 박스
        categoryField.setBounds(255, 3, 100, 30);
        categoryField.setFont(frontSetting.f16);

        JLabel peopleNumLabel = new JLabel("인원: ");  // 인원 수 라벨
        peopleNumLabel.setBounds(365, 3, 50, 30);
        peopleNumLabel.setFont(frontSetting.f18);

        JTextField peopleNumField = new JTextField();
        peopleNumField.setBounds(410, 3, 25, 30);
        peopleNumField.setFont(frontSetting.f18);

        JTextArea contentArea = new JTextArea(445, 250);  // 내용 작성 필드
        contentArea.setFont(frontSetting.f16);

        JScrollPane contentScroll = new JScrollPane(contentArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentScroll.setBounds(20, 185, 445, 250);
        contentScroll.setBorder(null);

        RoundedButton modifyBtn = new RoundedButton("올리기");  // 수정 버튼
        modifyBtn.setBounds(200, 480, 90, 50);
        modifyBtn.setFont(frontSetting.fb16);

        modifyBtn.addActionListener(new ActionListener() {  // 수정 버튼 클릭 시
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String region = (String) regionField.getSelectedItem();
                String category = (String) categoryField.getSelectedItem();
                String peopleNum = peopleNumField.getText();
                String content = contentArea.getText();

                if(modifyErrorCheck(title, region, category, peopleNum, content)) { // 오류 검출 후 DB 넘기기
                    System.out.println("수정: [" + title + ", " + region + ", " + category + ", " + peopleNum + ", " + content + "]");
                    modifyFrame.dispose();
                    setSuccessPopUpFrame();
                }
            }
        });

        c.add(newPostLabel);
        c.add(titleLabel);
        c.add(titleField);
        c.add(whitePanel);
        whitePanel.add(regionLabel);
        whitePanel.add(regionField);
        whitePanel.add(categoryLabel);
        whitePanel.add(categoryField);
        whitePanel.add(peopleNumLabel);
        whitePanel.add(peopleNumField);
        c.add(contentScroll);
        c.add(modifyBtn);

        modifyFrame.setVisible(true);
    }

    private boolean modifyErrorCheck(String title, String region, String category, String peopleNum, String content) {  // 빈칸 있는 지 확인 후 올리기
        System.out.println("오류 검사: [" + title + ", " + region + ", " + category + ", " + peopleNum + ", " + content + "]");  // 테스트

        boolean checkBlank = false;  // 빈 칸 & 공백 확인
        boolean checkPeopleNum = false;  // 인원 수 입력 조건 확인

        if (title.isBlank()) frontSetting.showErrorDialog("제목을 입력해주세요.");
        else if (region.equals(" --")) frontSetting.showErrorDialog("지역을 선택해주세요.");
        else if (category.equals(" --")) frontSetting.showErrorDialog("카테고리를 선택해주세요.");
        else if (peopleNum.isBlank()) frontSetting.showErrorDialog("인원 수를 입력해주세요.");
        else if (content.isBlank()) frontSetting.showErrorDialog("내용을 입력해주세요.");
        else {
            try {
                if (Integer.parseInt(peopleNum) > 30) frontSetting.showErrorDialog("인원은 30명까지 입력 가능합니다.");
                else if (Integer.parseInt(peopleNum) <= 1) frontSetting.showErrorDialog("인원은 2명부터 입력 가능합니다.");
                else checkPeopleNum = true;
            } catch (NumberFormatException e) { frontSetting.showErrorDialog("인원은 숫자만 입력 가능합니다."); }
            checkBlank = true;
        }

        if(checkBlank && checkPeopleNum) return true;  // 마지막 오류 검출
        else return false;
    }

    private void setSuccessPopUpFrame() {  // 수정 완료 팝업
        JFrame notifyFrame = new JFrame();
        notifyFrame.setSize(300, 200);
        frontSetting.FrameSetting(notifyFrame);

        Container c = notifyFrame.getContentPane();
        c.setLayout(null);
        c.setBackground(frontSetting.mainColor);


        JLabel successLabel = new JLabel("수정 완료");
        successLabel.setFont(frontSetting.fb16);
        successLabel.setBounds(110, 35, 200, 40);

        RoundedButton okBtn = new RoundedButton("확인");
        okBtn.setFont(frontSetting.fb16);
        okBtn.setBounds(105, 95, 80, 30);

        okBtn.addActionListener(new ActionListener() {  // ok 버튼 누르면 알림창 닫힘
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyFrame.dispose();
                centerPanel.revalidate();
                centerPanel.repaint();
                System.out.println("수정완");  // 수정 필요함
            }
        });

        c.add(successLabel);
        c.add(okBtn);

        notifyFrame.setVisible(true);
    }
}
