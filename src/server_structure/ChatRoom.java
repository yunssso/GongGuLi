package server_structure;

import back.handler.ChatRoom_Handler;

import java.net.ServerSocket;
import java.net.Socket;

public class ChatRoom extends Thread {
    private Socket clientSocket = null;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1026)) {
            while (true) {
                clientSocket = serverSocket.accept();
                new ChatRoom_Handler(clientSocket).start();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
