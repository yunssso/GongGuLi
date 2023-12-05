package main;

import back.dao.chatting.OpenChattingRoomDAO;
import serverStructure.*;

import java.net.ServerSocket;
import java.util.List;

public class ServerMain {
    public static void main(String[] args) {
        openChattingRoom();

        new Account().start();
        new Board().start();
        new ChatRoom().start();
        new BoardInfo().start();
        new MyPage().start();
    }

    public static void openChattingRoom() {
        try {
            List<Integer> ports = new OpenChattingRoomDAO().getAllPorts();

            for (int port : ports) {
                ServerSocket serverSocket = new ServerSocket(port);
                new ChatServer(serverSocket).start();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}