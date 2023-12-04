package front.myPage;

import back.response.ResponseCode;
import back.request.mypage.MyBoardInfoMoreRequest;
import back.request.mypage.MyBoardInfoRequest;
import back.response.mypage.MyBoardInfoMoreResponse;
import back.response.mypage.MyBoardInfoResponse;
import front.FrontSetting;
import front.ReadMorePost;

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

public class MyPost extends JFrame{
    private FrontSetting frontSetting = new FrontSetting();
    private String uuid;
    private JFrame myFrame;

    public MyPost(JPanel centerPanel, String uuid, JFrame myframe) {
        this.uuid = uuid;
        this.myFrame = myframe;
        getMyPostInfoMethod();

        setMyPostingPanel(centerPanel);
    }

    private void setMyPostingPanel(JPanel centerPanel) {
        JLabel myPostingLabel = new JLabel("내가 쓴 글");
        myPostingLabel.setFont(frontSetting.fb20);
        myPostingLabel.setBounds(155, 50, 150, 40);

        JPanel myPostingPanel = new JPanel(null);
        myPostingPanel.setBounds(30, 120, 340, 500);
        myPostingPanel.setBackground(Color.WHITE);

        JTable postTable = new JTable(frontSetting.myPageDB, frontSetting.myPageHeader) { // 셀 내용 수정 불가
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        frontSetting.tableSetting(postTable, frontSetting.myPostingTableWidths);

        JScrollPane myPostingScroll = new JScrollPane(postTable);
        myPostingScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        myPostingScroll.setBounds(0, 0, 340, 480);

        postTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try (Socket clientSocket = new Socket("43.200.49.16", 1027);
                     OutputStream outputStream = clientSocket.getOutputStream();
                     ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                     InputStream inputStream = clientSocket.getInputStream();
                     ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                ){

                    if(e.getClickCount() == 2) {
                        int selectRow = postTable.getSelectedRow();

                        MyBoardInfoMoreRequest myBoardInfoMoreRequest = new MyBoardInfoMoreRequest(selectRow, uuid);

                        objectOutputStream.writeObject(myBoardInfoMoreRequest);

                        ResponseCode responseCode = (ResponseCode) objectInputStream.readObject();

                        if (responseCode.getKey() == ResponseCode.BOARD_INFO_MORE_SUCCESS.getKey()) { //게시글 자세히 보기 성공
                            MyBoardInfoMoreResponse myBoardInfoMoreResponse = (MyBoardInfoMoreResponse) objectInputStream.readObject();
                            boolean isMainFrame = false;
                            new ReadMorePost(isMainFrame, uuid, myFrame, myBoardInfoMoreResponse);   // 작성자 본인인지 아닌지는 생성자에서 판단.
                        } else { //게시글 자세히 보기 실패
                            showErrorDialog(responseCode.getValue());
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        centerPanel.add(myPostingPanel);
        centerPanel.add(myPostingLabel);
        myPostingPanel.add(myPostingScroll);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "안내", JOptionPane.ERROR_MESSAGE);
    }

    /*내가 쓴 글을 서버에서 받아오는 메소드*/
    private void getMyPostInfoMethod() {
        try (Socket clientSocket = new Socket("43.200.49.16", 1028);
             OutputStream outputStream = clientSocket.getOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
             InputStream inputStream = clientSocket.getInputStream();
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ){
            MyBoardInfoRequest myBoardInfoRequest = new MyBoardInfoRequest(uuid); // uuid를 Request 객체로 만듦

            objectOutputStream.writeObject(myBoardInfoRequest); // uuid를 서버에 보내서 내가 쓴 글 요청

            ResponseCode responseCode = (ResponseCode) objectInputStream.readObject(); // 서버에서 응답 코드를 받아옴

            if (responseCode.getKey() == ResponseCode.GET_MY_BOARD_INFO_SUCCESS.getKey()) { // 내가 쓴 글 갱신 성공
                java.util.List<MyBoardInfoResponse> myBoardInfoResponseList = (List<MyBoardInfoResponse>) objectInputStream.readObject(); // 서버에서 내가 쓴 글을 받아옴

                frontSetting.setMyPageDB(myBoardInfoResponseList);
            } else { // 내가 쓴 글 갱신 실패
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
