package serverStructure;

import back.handler.MyPageHandler;

import java.net.ServerSocket;
import java.net.Socket;

public class MyPage extends Thread {
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1028)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new MyPageHandler(clientSocket).start();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}