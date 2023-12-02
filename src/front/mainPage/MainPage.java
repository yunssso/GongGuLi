package front.mainPage;

import back.ResponseCode;
import back.request.board.BoardInfoMoreRequest;
import back.request.board.BoardInfoRequest;
import back.response.board.BoardInfoMoreResponse;
import back.response.board.BoardInfoResponse;
import front.*;
import front.myPage.MyPage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class MainPage extends JFrame{
    FrontSetting frontSetting = new FrontSetting();

    private String region = " --";
    private String category = " --";

    private DefaultTableModel model;
    private JTable postTable = null;
    private JPanel centerPanel;
    private String uuid = null;

    private JFrame mainFrame = this;

    public MainPage() {

    }
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

                new MyPage(uuid);
            }
        });

        RoundedButton chattingListBtn = new RoundedButton("채팅 목록");  // 채팅 목록 버튼
        chattingListBtn.setBounds(120, 35, 130, 40);
        chattingListBtn.setFont(frontSetting.fb16);

        /*채팅 목록 버튼 클릭 시*/
        chattingListBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChattingList(uuid);
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
                try {
                    region = (String) regionBtn.getSelectedItem();
                    category = (String) categoryBtn.getSelectedItem();

                    refreshTable();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        /*카테고리 버튼 클릭 시*/
        categoryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    region = (String) regionBtn.getSelectedItem();
                    category = (String) categoryBtn.getSelectedItem();

                    refreshTable();
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
        centerPanel = new JPanel(null);
        centerPanel.setBounds(300, 0, 770, 700);
        centerPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField();  // 검색창
        searchField.setBounds(550, 40, 150, 30);
        searchField.setFont(frontSetting.f16);

        ImageIcon searchImg = new ImageIcon("img/searchBtn.png");
        JButton searchBtn = new JButton(searchImg);  // 검색 버튼
        searchBtn.setBorder(null);
        searchBtn.setBounds(702, 40, 30, 30 );

        String searchFilter[] = {"제목", "작성자"};
        JComboBox searchFilterBox = new JComboBox(searchFilter);
        searchFilterBox.setBounds(460, 40, 80, 30);

        // 검색창 엔터 이벤트
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println(searchField.getText());
            }
        });
        /*검색창에 입력 후 검색 버튼 누르면 텍스트 출력*/
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(searchField.getText());
            }
        });


        // 제목/작성자 검색 필터
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        getBoardInfoMethod();

        /*게시글 출력 아님. 세팅하는거일 뿐*/
        model = new DefaultTableModel(frontSetting.mainPageDB, frontSetting.mainPageHeader);
        postTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {  // 셀 내용 수정 불가 설정
                return false;
            }
        };
        frontSetting.tableSetting(postTable, frontSetting.mainTableWidths);  // postTable 세팅

//        자세히 보기
        postTable.addMouseListener(new MouseAdapter() {  // 테이블 값 더블 클릭 시
            @Override
            public void mouseClicked(MouseEvent e) {
                try (Socket clientSocket = new Socket("localhost", 1027);
                     OutputStream outputStream = clientSocket.getOutputStream();
                     ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                     InputStream inputStream = clientSocket.getInputStream();
                     ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                ){

                    if(e.getClickCount() == 2) {
                        int selectRow = postTable.getSelectedRow();

                        BoardInfoMoreRequest boardInfoMoreRequest = new BoardInfoMoreRequest(selectRow, uuid);

                        objectOutputStream.writeObject(boardInfoMoreRequest);

                        ResponseCode responseCode = (ResponseCode) objectInputStream.readObject();

                        if (responseCode.getKey() == ResponseCode.BOARD_INFO_MORE_SUCCESS.getKey()) { //게시글 자세히 보기 성공
                            BoardInfoMoreResponse boardInfoMoreResponse = (BoardInfoMoreResponse) objectInputStream.readObject();
                            boolean isMainFrame = true;
                            new ReadMorePost(isMainFrame, uuid, mainFrame, boardInfoMoreResponse);   // 작성자 본인인지 아닌지는 생성자에서 판단.
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
        newPostBtn.setFont(frontSetting.fb16);

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

    public void refreshTable() {
        getBoardInfoMethod();
        model = new DefaultTableModel(frontSetting.mainPageDB, frontSetting.mainPageHeader);
        postTable.setModel(model);
        frontSetting.tableSetting(postTable, frontSetting.mainTableWidths);
    }

    /*새 글 작성*/
    private void setPosting() {
        new NewPost(uuid, this);
    }

    /*에러 메시지*/
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "안내", JOptionPane.ERROR_MESSAGE);
    }

    /*게시글 정보 불러오는 함수*/
    private void getBoardInfoMethod() {
        try (Socket clientSocket = new Socket("localhost", 1027);
             OutputStream outputStream = clientSocket.getOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
             InputStream inputStream = clientSocket.getInputStream();
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ){

            //서버로 정보를 전달 해주기 위해서 객체 형식으로 변환
            BoardInfoRequest boardInfoRequest = new BoardInfoRequest(region, category, uuid);

            objectOutputStream.writeObject(boardInfoRequest);

            ResponseCode responseCode = (ResponseCode) objectInputStream.readObject();

            if (responseCode.getKey() == ResponseCode.BOARD_INFO_SUCCESS.getKey()) { //게시글 갱신 성공
                //boardList안에 레코드 형태에 게시글 정보가 다 들어있음.
                List <BoardInfoResponse> boardList = (List <BoardInfoResponse>) objectInputStream.readObject();

                frontSetting.setMainPageDB(boardList);

                System.out.println(frontSetting.getMainPageDB());
            } else { //게시글 갱신 실패
                showErrorDialog(responseCode.getValue());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}