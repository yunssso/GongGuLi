package back.server;

import back.server.handler.AccountHandler;

import java.net.ServerSocket;
import java.net.Socket;

class Account extends Thread {
    private Socket clientSocket = null;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1024);) {
            while (true) {
                clientSocket = serverSocket.accept();
                new AccountHandler(clientSocket).start();
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
}

class Post extends Thread {
    private Socket clientSocket = null;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1025);) {
            while (true) {
                clientSocket = serverSocket.accept();
                new AccountHandler(clientSocket).start();
            }
            
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
}

public class ServerMain {
    public static void main(String[] args) {
        new Account().start();
    }
}