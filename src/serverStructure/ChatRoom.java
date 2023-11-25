package serverStructure;

import back.handler.ChatRoomHandler;

import java.net.ServerSocket;
import java.net.Socket;

public class ChatRoom extends Thread {
    private Socket clientSocket = null;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1026)) {
            while (true) {
                clientSocket = serverSocket.accept();
                new ChatRoomHandler(clientSocket).start();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
