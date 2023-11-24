package front.MainPage;

import front.FrontSetting;

import javax.swing.*;
import java.awt.*;

public class ChattingList {
    FrontSetting frontSetting = new FrontSetting();

    public ChattingList() {
        setChattingList();
    }
    private void setChattingList() {
        JFrame chattingListFrame = new JFrame();  // 채팅 목록 팝업창 프레임
        chattingListFrame.setTitle("채팅 목록");
        chattingListFrame.setSize(400, 600);
        frontSetting.FrameSetting(chattingListFrame);

        JPanel chattingListPanel = new JPanel(null);  // 채팅 목록 팝업창 패널
        chattingListPanel.setBounds(0, 0, 400, 600);
        chattingListPanel.setBackground(frontSetting.mainColor);

        JLabel chattingListLabel = new JLabel("채팅 목록");  // 채팅 목록 레이블
        chattingListLabel.setFont(new Font("SUITE", Font.BOLD, 20));
        chattingListPanel.setBounds(10, 10, 100, 60);

        // 채팅 목록 출력
        String listHeader[] = {"카테고리", "제목", "작성자", "참여하기"};
        String listDB[][] = {{"", "", "", ""}};  // 차후 DB 연동
        JTable listTable = new JTable(listDB, listHeader);

        JScrollPane listScrollPane = new JScrollPane(listTable);
        listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScrollPane.setBounds(30, 80, 340, 430);

        chattingListFrame.add(chattingListPanel);
        chattingListPanel.add(chattingListLabel);
        chattingListPanel.add(listScrollPane);

        chattingListFrame.setVisible(true);
    }
}
