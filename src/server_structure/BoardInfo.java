package server_structure;

import back.handler.Board_Info_Handler;

import java.net.ServerSocket;
import java.net.Socket;

public class BoardInfo extends Thread {
    private Socket clientSocket = null;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1027)) {
            while (true) {
                clientSocket = serverSocket.accept();
                new Board_Info_Handler(clientSocket).start();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
