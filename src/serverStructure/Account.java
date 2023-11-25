package serverStructure;

import back.handler.AccountHandler;

import java.net.ServerSocket;
import java.net.Socket;

public class Account extends Thread {
    private Socket clientSocket = null;

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(1024);
            while (true) {
                clientSocket = serverSocket.accept();
                new AccountHandler(clientSocket).start();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
