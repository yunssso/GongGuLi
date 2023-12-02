package front.myPage;

import back.response.ResponseCode;
import back.request.mypage.MyHistoryInfoRequest;
import back.response.board.BoardInfoMoreResponse;
import back.response.mypage.MyHistoryInfoResponse;
import front.FrontSetting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class MyHistory {
    private FrontSetting fs = new FrontSetting();
    private String uuid;

    public MyHistory(JPanel centerPanel, String uuid) {
        this.uuid = uuid;
        getMyHistoryInfoMethod();
        setMyHistoryPanel(centerPanel);
    }

    private void setMyHistoryPanel(JPanel centerPanel) {
        JLabel myHistoryLabel = new JLabel("나의 공구 내역");
        myHistoryLabel.setFont(fs.fb20);
        myHistoryLabel.setBounds(510, 50, 150, 40);

        JPanel myHistoryPanel = new JPanel(null);
        myHistoryPanel.setBounds(400, 120, 340, 480);
        myHistoryPanel.setBackground(Color.WHITE);

        JTable myHistoryTable = new JTable(fs.myHistoryDB, fs.mainPageHeader) { // 셀 내용 수정 불가
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        fs.tableSetting(myHistoryTable, fs.myHistoryTableWidths);

        JScrollPane myHistoryScroll = new JScrollPane(myHistoryTable);
        myHistoryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        myHistoryScroll.setBounds(0, 0, 340, 480);

        myHistoryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    int selectRow = myHistoryTable.getSelectedRow();
                    //Board_Info_More_Response boardInfoMoreResponse = boardDAO.readMorePost(port, uuid);
                    //readMoreMyHistory(myHistoryTable, port, boardInfoMoreResponse);
                }
            }
        });

        centerPanel.add(myHistoryLabel);
        centerPanel.add(myHistoryPanel);
        myHistoryPanel.add(myHistoryScroll);
    }

    public void readMoreMyHistory(JTable t, int selectRow, BoardInfoMoreResponse boardInfoMoreResponse) {  // 테이블 값 더블 클릭 시 자세히보기
        System.out.println(t.getValueAt(selectRow, 2));

        JFrame readMoreFrame = new JFrame(boardInfoMoreResponse.title());  // 자세히보기 팝업창 프레임
        readMoreFrame.setSize(500, 600);
        fs.FrameSetting(readMoreFrame);

        Container c = readMoreFrame.getContentPane();  // 자세히보기 팝업창 패널
        c.setLayout(null);
        c.setBackground(fs.mainColor);

        JLabel logoLabel = new JLabel("공구리");  // 라벨
        logoLabel.setFont(fs.fb20);
        logoLabel.setBounds(220, 20, 100, 40);

        JTextArea titleArea = new JTextArea(" 제목: " + boardInfoMoreResponse.title());
        titleArea.setBounds(20, 80, 445, 35);
        titleArea.setFont(fs.f18);
        titleArea.setEditable(false);

        JTextArea infoArea1 = new JTextArea(" 지역: " + boardInfoMoreResponse.region() +
                "\n 글쓴이: " + boardInfoMoreResponse.nickName());
        infoArea1.setBounds(20, 125, 230, 55);
        infoArea1.setFont(fs.f18);
        infoArea1.setEditable(false);

        JTextArea infoArea2 = new JTextArea("카테고리: " + boardInfoMoreResponse.category() +
                "\n현황: " + boardInfoMoreResponse.peopleNum());
        infoArea2.setBounds(250, 125, 215, 55);
        infoArea2.setFont(fs.f18);
        infoArea2.setEditable(false);

        JTextArea contentArea = new JTextArea(boardInfoMoreResponse.content());
        contentArea.setBounds(20, 210, 445, 250);
        contentArea.setFont(fs.f18);
        contentArea.setEditable(false);
        contentArea.setDragEnabled(false);

        JLabel viewCountLabel = new JLabel("조회수: " + boardInfoMoreResponse.view());
        viewCountLabel.setFont(fs.f14);
        viewCountLabel.setBounds(20, 465, 150, 20);

        c.add(logoLabel);
        c.add(titleArea);
        c.add(infoArea1);
        c.add(infoArea2);
        c.add(contentArea);
        c.add(viewCountLabel);

        readMoreFrame.setVisible(true);
    }

    /*내가 참여한 글을 서버에서 받아오는 메소드*/
    private void getMyHistoryInfoMethod() {
        try (Socket clientSocket = new Socket("localhost", 1028);
             OutputStream outputStream = clientSocket.getOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
             InputStream inputStream = clientSocket.getInputStream();
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ){
            MyHistoryInfoRequest myHistoryInfoRequest = new MyHistoryInfoRequest(uuid); // uuid를 Request 객체로 만듦

            objectOutputStream.writeObject(myHistoryInfoRequest); // uuid를 서버에 보내서 내가 쓴 글 요청

            ResponseCode responseCode = (ResponseCode) objectInputStream.readObject(); // 서버에서 응답 코드를 받아옴

            if (responseCode.getKey() == ResponseCode.GET_MY_HISTORY_INFO_SUCCESS.getKey()) { // 내가 쓴 글 갱신 성공
                java.util.List<MyHistoryInfoResponse> myHistoryInfoResponseList = (List<MyHistoryInfoResponse>) objectInputStream.readObject(); // 서버에서 내가 참여한 거래를 받아옴

                fs.setMyHistoryDB(myHistoryInfoResponseList);
            } else { // 내가 쓴 글 갱신 실패
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
