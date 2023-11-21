package server_structure;

import back.handler.Board_Handler;

import java.net.ServerSocket;
import java.net.Socket;

public class Board extends Thread {
    private Socket clientSocket = null;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1025)) {
            while (true) {
                clientSocket = serverSocket.accept();
                new Board_Handler(clientSocket).start();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
