package serverStructure;

import back.handler.BoardHandler;

import java.net.ServerSocket;
import java.net.Socket;

public class Board extends Thread {
    private Socket clientSocket = null;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1025)) {
            while (true) {
                clientSocket = serverSocket.accept();
                new BoardHandler(clientSocket).start();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
