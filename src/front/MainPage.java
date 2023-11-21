package front;

import back.ResponseCode;
import back.request.board.Board_Info_More_Request;
import back.request.board.Board_Info_Request;
import back.request.chatroom.Join_ChatRoom_Request;
import back.request.board.Post_Board_Request;
import back.response.board.Board_Info_More_Response;
import back.response.board.Board_Info_Response;
import back.response.chatroom.Join_ChatRoom_Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class MainPage extends JFrame{
    private final FrontSetting frontSetting = new FrontSetting();

    private String region = " --";
    private String category = " --";

    private JTable postTable = null;
    private JScrollPane listScrollPane = null;

    private String uuid = null;

    /*생성자*/
    public MainPage(String uuid) {
        this.uuid = uuid;

        setListFrame();
        setLeftPanel();
        setCenterPanel();
        setRightPanel();

        setVisible(true);
    }

    /*기본 프레임 세팅*/
    public void setListFrame() {
        setSize(1120, 700);
        frontSetting.FrameSetting(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("공구리 게시글 페이지");
    }

    /*왼쪽 색 바 (프로필, 카테고리)*/
    private void setLeftPanel() {
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(0, 0, 300, 700);
        leftPanel.setBackground(frontSetting.mainColor);

        ImagePanel logoImg = new ImagePanel(new ImageIcon("img/logo2.png").getImage());
        logoImg.setBounds(50, 430, 200, 200);

        ImageIcon profileImgIcon = new ImageIcon("img/profile2.png");
        JButton profileBtn = new JButton(profileImgIcon);
        profileBtn.setBounds(30, 30, 50, 50);
        profileBtn.setBorder(null);

        /*유저 프로필 버튼 클릭 시*/
        profileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                MyPage mp = new MyPage();
                mp.setMyPage();
            }
        });

        RoundedButton chattingListBtn = new RoundedButton("채팅 목록");  // 채팅 목록 버튼
        chattingListBtn.setBounds(120, 35, 130, 40);
        chattingListBtn.setFont(frontSetting.f16);

        /*채팅 목록 버튼 클릭 시*/
        chattingListBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setChattingList();
            }
        });

        JLabel regionLabel = new JLabel("지역 필터");  // 지역 필터 레이블
        regionLabel.setBounds(72, 170, 120, 30);
        regionLabel.setFont(frontSetting.fb16);

        JComboBox regionBtn = new JComboBox(frontSetting.regionArr);  // 지역 필터 버튼
        regionBtn.setBounds(70, 200, 170, 35);
        regionBtn.setFont(frontSetting.f18);

        JLabel categoryLabel = new JLabel("카테고리 필터");  // 카테고리 필터 레이블
        categoryLabel.setBounds(72, 270, 120, 30);
        categoryLabel.setFont(frontSetting.fb16);

        JComboBox categoryBtn = new JComboBox(frontSetting.categoryArr);  // 카테고리 필터 버튼
        categoryBtn.setBounds(70, 300, 170, 35);
        categoryBtn.setFont(frontSetting.f18);

        /*지역 버튼 선택 시*/
        regionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Socket clientSocket = new Socket("localhost", 1027);
                     OutputStream os = clientSocket.getOutputStream();
                     ObjectOutputStream oos = new ObjectOutputStream(os);
                     InputStream is = clientSocket.getInputStream();
                     ObjectInputStream ois = new ObjectInputStream(is);
                     ){

                    region = (String) regionBtn.getSelectedItem();
                    category = (String) categoryBtn.getSelectedItem();

                    Board_Info_Request boardInfoRequest = new Board_Info_Request(region, category, uuid);

                    oos.writeObject(boardInfoRequest);

                    ResponseCode responseCode = (ResponseCode) ois.readObject();

                    if (responseCode.getKey() == ResponseCode.BOARD_INFO_SUCCESS.getKey()) { //게시글 갱신 성공
                        //boardList안에 레코드 형태에 게시글 정보가 다 들어있음.
                        List <Board_Info_Response> boardList = (List <Board_Info_Response>) ois.readObject();

                        frontSetting.setmainPageDB(boardList);

                        postTable = new JTable(frontSetting.mainPageDB, frontSetting.mainPageHeader) {
                            @Override
                            public boolean isCellEditable(int row, int column) {  // 셀 내용 수정 불가 설정
                                return false;
                            }
                        };

                        frontSetting.tableSetting(postTable, frontSetting.mainTableWidths);
                    } else { //게시글 갱신 실패
                        showErrorDialog(responseCode.getValue());
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        /*카테고리 버튼 클릭 시*/
        categoryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Socket clientSocket = new Socket("localhost", 1027);
                     OutputStream os = clientSocket.getOutputStream();
                     ObjectOutputStream oos = new ObjectOutputStream(os);
                     InputStream is = clientSocket.getInputStream();
                     ObjectInputStream ois = new ObjectInputStream(is);
                     ){

                    region = (String) regionBtn.getSelectedItem();
                    category = (String) categoryBtn.getSelectedItem();

                    Board_Info_Request boardInfoRequest = new Board_Info_Request(region, category, uuid);

                    oos.writeObject(boardInfoRequest);

                    ResponseCode responseCode = (ResponseCode) ois.readObject();

                    if (responseCode.getKey() == ResponseCode.BOARD_INFO_SUCCESS.getKey()) { //게시글 갱신 성공
                        //boardList안에 레코드 형태에 게시글 정보가 다 들어있음.
                        List <Board_Info_Response> boardList = (List <Board_Info_Response>) ois.readObject();

                        frontSetting.setmainPageDB(boardList);
                    } else { //게시글 갱신 실패
                        showErrorDialog(responseCode.getValue());
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        add(leftPanel);
        leftPanel.add(logoImg);
        leftPanel.add(profileBtn);
        leftPanel.add(chattingListBtn);
        leftPanel.add(regionLabel);
        leftPanel.add(categoryLabel);
        leftPanel.add(regionBtn);
        leftPanel.add(categoryBtn);
    }

    /*게시글 패널*/
    private void setCenterPanel() {
        JPanel centerPanel = new JPanel(null);
        centerPanel.setBounds(300, 0, 770, 700);
        centerPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField();  // 검색창
        searchField.setBounds(550, 40, 150, 30);
        searchField.setFont(frontSetting.f16);

        ImageIcon searchImg = new ImageIcon("img/searchBtn.png");
        JButton searchBtn = new JButton(searchImg);  // 검색 버튼
        searchBtn.setBorder(null);
        searchBtn.setBounds(702, 40, 30, 30 );

        String searchFilter[] =  {"제목", "작성자"};
        JComboBox searchFilterBox = new JComboBox(searchFilter);
        searchFilterBox.setBounds(460, 40, 80, 30);

        getBoardInfoMethod();

        /*검색창에 입력 후 검색 버튼 누르면 텍스트 출력*/
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(searchField.getText());
            }
        });

        /*게시글 출력*/
        postTable = new JTable(frontSetting.mainPageDB, frontSetting.mainPageHeader) {
            @Override
            public boolean isCellEditable(int row, int column) {  // 셀 내용 수정 불가 설정
                return false;
            }
        };

        frontSetting.tableSetting(postTable, frontSetting.mainTableWidths);  // postTable 세팅

        postTable.addMouseListener(new MouseAdapter() {  // 테이블 값 더블 클릭 시
            @Override
            public void mouseClicked(MouseEvent e) {
                try (Socket clientSocket = new Socket("localhost", 1027);
                     OutputStream os = clientSocket.getOutputStream();
                     ObjectOutputStream oos = new ObjectOutputStream(os);
                     InputStream is = clientSocket.getInputStream();
                     ObjectInputStream ois = new ObjectInputStream(is);
                     ){

                    if(e.getClickCount() == 2) {
                        int selectRow = postTable.getSelectedRow();

                        Board_Info_More_Request boardInfoMoreRequest = new Board_Info_More_Request(selectRow, uuid);

                        oos.writeObject(boardInfoMoreRequest);

                        ResponseCode responseCode = (ResponseCode) ois.readObject();

                        if (responseCode.getKey() == ResponseCode.BOARD_INFO_MORE_SUCCESS.getKey()) { //게시글 자세히 보기 성공
                            Board_Info_More_Response boardInfoMoreResponse = (Board_Info_More_Response) ois.readObject();

                            if (boardInfoMoreResponse.authority()) { //글쓴이인 경우
                                readMoreMyPost(postTable, selectRow, boardInfoMoreResponse);
                            } else { //글쓴이가 아닌 경우
                                readMorePost(postTable, boardInfoMoreResponse);
                            }
                        } else { //게시글 자세히 보기 실패
                            showErrorDialog(responseCode.getValue());
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        JScrollPane listScrollPane = new JScrollPane(postTable);  // 게시글 스크롤 생성
        listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScrollPane.setBounds(50, 110, 680, 470);

        RoundedButtonR newPostBtn = new RoundedButtonR("새 글");  // 새 글 작성 버튼
        newPostBtn.setBounds(638, 600, 90, 40);
        newPostBtn.setFont(frontSetting.f16);

        newPostBtn.addActionListener(new ActionListener() {  // 새 글 버튼 클릭 시
            @Override
            public void actionPerformed(ActionEvent e) {
                setPosting();
            }
        });

        add(centerPanel);
        centerPanel.add(searchField);
        centerPanel.add(searchBtn);
        centerPanel.add(searchFilterBox);
        centerPanel.add(listScrollPane);
        centerPanel.add(newPostBtn);
    }

    /*오른쪽 색 바*/
    private void setRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(1070, 0, 50, 700);
        rightPanel.setBackground(frontSetting.mainColor);

        add(rightPanel);
    }

    /*채팅 목록*/
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

        listScrollPane = new JScrollPane(listTable);
        listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScrollPane.setBounds(30, 80, 340, 430);

        chattingListFrame.add(chattingListPanel);
        chattingListPanel.add(chattingListLabel);
        chattingListPanel.add(listScrollPane);

        chattingListFrame.setVisible(true);
    }

    /*테이블 값 더블 클릭 시 자세히 보기*/
    public void readMorePost(JTable t, Board_Info_More_Response boardInfoMoreResponse) {
        JFrame readMoreFrame = new JFrame(boardInfoMoreResponse.title());  // 자세히보기 팝업창 프레임
        readMoreFrame.setSize(500, 600);
        frontSetting.FrameSetting(readMoreFrame);

        Container c = readMoreFrame.getContentPane();  // 자세히보기 팝업창 패널
        c.setLayout(null);
        c.setBackground(frontSetting.mainColor);

        JLabel logoLabel = new JLabel("공구리");  // 라벨
        logoLabel.setFont(frontSetting.fb20);
        logoLabel.setBounds(220, 20, 100, 40);

        JTextArea titleArea = new JTextArea(" 제목: " + boardInfoMoreResponse.title());
        titleArea.setBounds(20, 80, 445, 35);
        titleArea.setFont(frontSetting.f18);
        titleArea.setEditable(false);

        JTextArea infoArea1 = new JTextArea(" 지역: " + boardInfoMoreResponse.region() +
                "\n 글쓴이: " + boardInfoMoreResponse.nickName());
        infoArea1.setBounds(20, 125, 230, 55);
        infoArea1.setFont(frontSetting.f18);
        infoArea1.setEditable(false);

        JTextArea infoArea2 = new JTextArea("카테고리: " + boardInfoMoreResponse.category() +
                "\n현황: " + boardInfoMoreResponse.peopleNum());
        infoArea2.setBounds(250, 125, 215, 55);
        infoArea2.setFont(frontSetting.f18);
        infoArea2.setEditable(false);

        JTextArea contentArea = new JTextArea(" " + boardInfoMoreResponse.content());
        contentArea.setBounds(20, 210, 445, 250);
        contentArea.setFont(frontSetting.f18);
        contentArea.setEditable(false);
        contentArea.setDragEnabled(false);

        JLabel viewCountLabel = new JLabel("조회수: " + boardInfoMoreResponse.view());
        viewCountLabel.setFont(frontSetting.f14);
        viewCountLabel.setBounds(20, 465, 150, 20);

        RoundedButton joinChatRoomBtn = new RoundedButton("채팅 참여");
        joinChatRoomBtn.setBounds(190, 480, 110, 50);
        joinChatRoomBtn.setFont(frontSetting.fb16);

        /*채팅 참여 버튼 클릭시*/
        joinChatRoomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Socket clientSocket = new Socket("localhost", 1026);
                     OutputStream os = clientSocket.getOutputStream();
                     ObjectOutputStream oos = new ObjectOutputStream(os);
                     InputStream is = clientSocket.getInputStream();
                     ObjectInputStream ois = new ObjectInputStream(is);
                     ){

                    int selectRow = 0; //여기에 사용자가 선택한 게시글 id를 받아와야 돼
                    Join_ChatRoom_Request joinChatroomRequest = new Join_ChatRoom_Request(selectRow, uuid);

                    oos.writeObject(joinChatroomRequest);

                    ResponseCode responseCode = (ResponseCode) ois.readObject();

                    if (responseCode.getKey() == ResponseCode.JOIN_CHATROOM_SUCCESS.getKey()) { //채팅방 입장 성공
                        Join_ChatRoom_Response joinChatroomResponse = (Join_ChatRoom_Response) ois.readObject();

                        //서버에서 받아온 포트 정보로 채팅방 클라이언트를 실행해서 접속 해준다.
                        //채팅방 랜덤 포트는 애당초 게시글을 생성할때 같이 넣어둬야 한다.
                        //new ChatClient(joinChatroomResponse.port());
                    } else { //채팅방 입장 실패
                        showErrorDialog(responseCode.getValue());
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        c.add(logoLabel);
        c.add(titleArea);
        c.add(infoArea1);
        c.add(infoArea2);
        c.add(contentArea);
        c.add(joinChatRoomBtn);
        c.add(viewCountLabel);

        readMoreFrame.setVisible(true);
    }

    /*테이블 값 더블 클릭 시 자세히보기*/
    public void readMoreMyPost(JTable t, int selectRow, Board_Info_More_Response boardInfoMoreResponse) {
        JFrame readMoreFrame = new JFrame(boardInfoMoreResponse.title());  // 자세히보기 팝업창 프레임
        readMoreFrame.setSize(500, 600);
        frontSetting.FrameSetting(readMoreFrame);

        Container c = readMoreFrame.getContentPane();  // 자세히보기 팝업창 패널
        c.setLayout(null);
        c.setBackground(frontSetting.mainColor);

        JLabel logoLabel = new JLabel("공구리");  // 라벨
        logoLabel.setFont(frontSetting.fb20);
        logoLabel.setBounds(220, 20, 100, 40);

        JTextArea titleArea = new JTextArea(" 제목: " + boardInfoMoreResponse.title());
        titleArea.setBounds(20, 80, 445, 35);
        titleArea.setFont(frontSetting.f18);
        titleArea.setEditable(false);

        JTextArea infoArea1 = new JTextArea(" 지역: " + boardInfoMoreResponse.region() +
                "\n 현황: " + boardInfoMoreResponse.peopleNum());
        infoArea1.setBounds(20, 125, 230, 55);
        infoArea1.setFont(frontSetting.f18);
        infoArea1.setEditable(false);

        JTextArea infoArea2 = new JTextArea("카테고리: " + boardInfoMoreResponse.category());
        infoArea2.setBounds(250, 125, 215, 55);
        infoArea2.setFont(frontSetting.f18);
        infoArea2.setEditable(false);

        JTextArea contentArea = new JTextArea(boardInfoMoreResponse.content());
        contentArea.setBounds(20, 210, 445, 250);
        contentArea.setFont(frontSetting.f18);
        contentArea.setEditable(false);
        contentArea.setDragEnabled(false);

        JLabel viewCountLabel = new JLabel("조회수: " + boardInfoMoreResponse.view());
        viewCountLabel.setFont(frontSetting.f14);
        viewCountLabel.setBounds(20, 465, 150, 20);

        RoundedButton modifyPostBtn = new RoundedButton("수정하기");
        modifyPostBtn.setBounds(190, 480, 110, 50);
        modifyPostBtn.setFont(frontSetting.fb16);

        modifyPostBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readMoreFrame.dispose();
                //getTitle, 등 값을 얻어 modifyMyPost()에 매개변수로 넘기기
                //modifyMyPost();
            }
        });

        JButton deletePostBtn = new JButton("삭제하기");
        deletePostBtn.setBounds(400, 465, 70, 20);
        deletePostBtn.setBackground(null);
        deletePostBtn.setBorder(null);
        deletePostBtn.setFont(frontSetting.f14);
        deletePostBtn.setForeground(frontSetting.c3);

        deletePostBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int answer = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    System.out.println("삭제");
                } else System.out.println("삭제 안함");
            }
        });


        c.add(logoLabel);
        c.add(titleArea);
        c.add(infoArea1);
        c.add(infoArea2);
        c.add(contentArea);
        c.add(modifyPostBtn);
        c.add(deletePostBtn);
        c.add(viewCountLabel);

        readMoreFrame.setVisible(true);
    }

    /*새 글 작성*/
    private void setPosting() {
        JFrame newPostFrame = new JFrame("새 글");   // 새 글 프레임
        newPostFrame.setSize(500, 600);
        frontSetting.FrameSetting(newPostFrame);

        Container c = newPostFrame.getContentPane();  // 새 글 팝업창 패널
        c.setBackground(frontSetting.mainColor);
        c.setLayout(null);

        JLabel newPostLabel = new JLabel("새 글");  // 라벨
        newPostLabel.setFont(frontSetting.fb20);
        newPostLabel.setBounds(220, 20, 100, 40);

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

        RoundedButton postBtn = new RoundedButton("올리기");  // 올리기 버튼
        postBtn.setBounds(200, 480, 90, 50);
        postBtn.setFont(frontSetting.fb16);

        postBtn.addActionListener(new ActionListener() {  // 올리기 버튼 클릭 시
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Socket clientSocket = new Socket("localhost", 1025);
                     OutputStream os = clientSocket.getOutputStream();
                     ObjectOutputStream oos = new ObjectOutputStream(os);
                     InputStream is = clientSocket.getInputStream();
                     ObjectInputStream ois = new ObjectInputStream(is);
                     ){

                    String title = titleField.getText();
                    String region = (String) regionField.getSelectedItem();
                    String category = (String) categoryField.getSelectedItem();
                    String peopleNum = peopleNumField.getText();
                    String content = contentArea.getText();

                    Post_Board_Request Post_BoardInfo = new Post_Board_Request(title, region, category, peopleNum, content, uuid);

                    oos.writeObject(Post_BoardInfo);

                    ResponseCode responseCode = (ResponseCode) ois.readObject();

                    if (responseCode.getKey() == ResponseCode.POST_BOARD_SUCCESS.getKey()) { //게시글 생성 성공
                        setSuccessPopUpFrame(responseCode.getValue());
                        new MainPage(uuid);
                    } else { //게시글 생성 실패
                        showErrorDialog(responseCode.getValue());
                    }

                    newPostFrame.dispose();
                } catch (Exception exception) {
                    exception.printStackTrace();
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
        c.add(postBtn);

        newPostFrame.setVisible(true);
    }

    /*에러 메시지*/
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "안내", JOptionPane.ERROR_MESSAGE);
    }

    /*성공 메세지*/
    private void setSuccessPopUpFrame(String message) {
        JFrame notifyFrame = new JFrame();
        notifyFrame.setSize(300, 200);
        frontSetting.FrameSetting(notifyFrame);

        Container c = notifyFrame.getContentPane();
        c.setLayout(null);
        c.setBackground(frontSetting.mainColor);


        JLabel successLabel = new JLabel(message);
        successLabel.setFont(frontSetting.fb16);
        successLabel.setBounds(100, 35, 200, 40);

        RoundedButton okBtn = new RoundedButton("확인");
        okBtn.setFont(frontSetting.fb16);
        okBtn.setBounds(105, 95, 80, 30);

        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyFrame.dispose();
                dispose();

                new MainPage(uuid);
            }
        });

        c.add(successLabel);
        c.add(okBtn);

        notifyFrame.setVisible(true);
    }

    /*게시글 정보 불러오는 함수*/
    private void getBoardInfoMethod() {
        try (Socket clientSocket = new Socket("localhost", 1027);
             OutputStream os = clientSocket.getOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(os);
             InputStream is = clientSocket.getInputStream();
             ObjectInputStream ois = new ObjectInputStream(is);
             ){

            //서버로 정보를 전달 해주기 위해서 객체 형식으로 변환
            Board_Info_Request boardInfoRequest = new Board_Info_Request(" --", " --", uuid);

            oos.writeObject(boardInfoRequest);

            ResponseCode responseCode = (ResponseCode) ois.readObject();

            if (responseCode.getKey() == ResponseCode.BOARD_INFO_SUCCESS.getKey()) { //게시글 갱신 성공
                //boardList안에 레코드 형태에 게시글 정보가 다 들어있음.
                List <Board_Info_Response> boardList = (List <Board_Info_Response>) ois.readObject();

                frontSetting.setmainPageDB(boardList);
            } else { //게시글 갱신 실패
                showErrorDialog(responseCode.getValue());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}