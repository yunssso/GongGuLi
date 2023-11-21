package server_structure;

import back.handler.Account_Handler;

import java.net.ServerSocket;
import java.net.Socket;

public class Account extends Thread {
    private Socket clientSocket = null;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1024)) {
            while (true) {
                clientSocket = serverSocket.accept();
                new Account_Handler(clientSocket).start();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
