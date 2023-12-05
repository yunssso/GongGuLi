package front;

import back.response.ResponseCode;
import back.request.board.ModifyMyPostRequest;
import back.response.board.BoardInfoMoreResponse;
import front.mainPage.MainPage;
import front.myPage.MyPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ModifyMyPost{

    private FrontSetting fs = new FrontSetting();
    int port;
    BoardInfoMoreResponse boardInfoMoreResponse;

    private String uuid;
    private JFrame frame;
    private boolean isMainFrame;

    public ModifyMyPost(JFrame myFrame, String uuid, BoardInfoMoreResponse boardInfoMoreResponse, int port) {
        this.frame = myFrame;
        this.uuid = uuid;
        this.port = port;
        this.isMainFrame = false;
        this.boardInfoMoreResponse = boardInfoMoreResponse;
        modifyMyPost();
    }

    public ModifyMyPost(JFrame mainframe, String uuid, int port, BoardInfoMoreResponse boardInfoMoreResponse) {
        this.frame = mainframe;
        this.uuid = uuid;
        this.boardInfoMoreResponse = boardInfoMoreResponse;
        this.port = port;
        this.isMainFrame = true;
        modifyMyPost();
    }

    private void modifyMyPost() {  // 글 수정하기
        JFrame modifyFrame = new JFrame("수정하기");   // 수정 프레임
        modifyFrame.setSize(500, 600);
        fs.FrameSetting(modifyFrame);

        Container c = modifyFrame.getContentPane();  // 수정 컨테이너
        c.setBackground(fs.mainColor);
        c.setLayout(null);

        JLabel newPostLabel = new JLabel("수정하기");  // 라벨
        newPostLabel.setFont(fs.fb20);
        newPostLabel.setBounds(210, 20, 100, 40);

        JLabel titleLabel = new JLabel("  제목:");  // 제목 라벨
        titleLabel.setBounds(20, 80, 60, 35);
        titleLabel.setFont(fs.f18);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.WHITE);

        JTextField titleField = new JTextField();  // 제목 입력 필드
        titleField.setText(boardInfoMoreResponse.title());
        titleField.setBounds(80, 80, 385, 35);
        titleField.setFont(fs.f18);
        titleField.setBorder(null);

        JPanel whitePanel = new JPanel(null);  // 지역, 카테고리의 흰색 배경
        whitePanel.setBounds(20, 125, 445, 35);
        whitePanel.setBackground(Color.WHITE);

        JLabel regionLabel = new JLabel(" 지역: ");  // 지역 라벨
        regionLabel.setBounds(5, 3, 70, 30);
        regionLabel.setFont(fs.f18);

        JComboBox regionField = new JComboBox(fs.regionArr);  // 지역 콤보 박스
        regionField.setSelectedItem(boardInfoMoreResponse.region());
        regionField.setBounds(55, 3, 100, 30);
        regionField.setFont(fs.f16);

        JLabel categoryLabel = new JLabel("카테고리: ");  // 카테고리 라벨
        categoryLabel.setBounds(170, 3, 100, 30);
        categoryLabel.setFont(fs.f18);

        JComboBox categoryField = new JComboBox(fs.categoryArr);  // 카테고리 콤보 박스
        categoryField.setSelectedItem(boardInfoMoreResponse.category());
        categoryField.setBounds(247, 3, 100, 30);
        categoryField.setFont(fs.f16);

        JLabel peopleNumLabel = new JLabel("인원: " + boardInfoMoreResponse.peopleNum());  // 인원 수 라벨
        peopleNumLabel.setBounds(362, 3, 100, 30);
        peopleNumLabel.setFont(fs.f18);

        JTextArea contentArea = new JTextArea(445, 250);  // 내용 작성 필드
        contentArea.setText(boardInfoMoreResponse.content());
        contentArea.setFont(fs.f16);
        contentArea.setLineWrap(true);

        JScrollPane contentScroll = new JScrollPane(contentArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentScroll.setBounds(20, 185, 445, 250);
        contentScroll.setBorder(null);

        RoundedButton modifyBtn = new RoundedButton("올리기");  // 수정 버튼
        modifyBtn.setBounds(200, 470, 90, 50);
        modifyBtn.setFont(fs.fb16);

        modifyBtn.addActionListener(new ActionListener() {  // 수정 버튼 클릭 시
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String region = (String) regionField.getSelectedItem();
                String category = (String) categoryField.getSelectedItem();
                String content = contentArea.getText();

                if(modifyErrorCheck(title, region, category, content)) { // 오류 검출 후 DB 넘기기
                    System.out.println("수정: [" + title + ", " + region + ", " + category + ", " + content + "]");

                    try (Socket clientSocket = new Socket("43.200.49.16", 1025);
                         OutputStream outputStream = clientSocket.getOutputStream();
                         ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                         InputStream inputStream = clientSocket.getInputStream();
                         ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                    ){
                        ModifyMyPostRequest modifyMyPostRequest = new ModifyMyPostRequest(port, title, region, category, content);

                        objectOutputStream.writeObject(modifyMyPostRequest);

                        ResponseCode responseCode = (ResponseCode) objectInputStream.readObject();

                        if (responseCode.getKey() == ResponseCode.MODIFY_MY_BOARD_SUCCESS.getKey()) {
                            frame.dispose();
                            if(isMainFrame) new MainPage(uuid);
                            else new MyPage(uuid);

                            modifyFrame.dispose();

                            setSuccessPopUpFrame();

                            System.out.println("수정완");
                        } else {
                            fs.showErrorDialog(responseCode.getValue());
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
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
        c.add(contentScroll);
        c.add(modifyBtn);

        modifyFrame.setVisible(true);
    }

    private boolean modifyErrorCheck(String title, String region, String category, String content) {  // 빈칸 있는 지 확인 후 올리기
        System.out.println("오류 검사: [" + title + ", " + region + ", " + category + ", " + content + "]");  // 테스트

        boolean checkBlank = false;  // 빈 칸 & 공백 확인

        if (title.isBlank()) fs.showErrorDialog("제목을 입력해주세요.");
        else if (region.equals(" --")) fs.showErrorDialog("지역을 선택해주세요.");
        else if (category.equals(" --")) fs.showErrorDialog("카테고리를 선택해주세요.");
        else if (content.isBlank()) fs.showErrorDialog("내용을 입력해주세요.");
        else checkBlank = true;

        if(checkBlank) return true;  // 마지막 오류 검출
        else return false;
    }

    private void setSuccessPopUpFrame() {  // 수정 완료 팝업
        JFrame notifyFrame = new JFrame();
        notifyFrame.setSize(300, 200);
        fs.FrameSetting(notifyFrame);

        Container c = notifyFrame.getContentPane();
        c.setLayout(null);
        c.setBackground(fs.mainColor);


        JLabel successLabel = new JLabel("수정 완료");
        successLabel.setFont(fs.fb16);
        successLabel.setBounds(110, 35, 200, 40);

        RoundedButton okBtn = new RoundedButton("확인");
        okBtn.setFont(fs.fb16);
        okBtn.setBounds(105, 95, 80, 30);

        okBtn.addActionListener(new ActionListener() {  // ok 버튼 누르면 알림창 닫힘
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyFrame.dispose();
            }
        });
        c.add(successLabel);
        c.add(okBtn);

        notifyFrame.setVisible(true);
    }
}
