package main;

import back.handler.Account_Handler;
import back.handler.Board_Handler;
import back.handler.ChatRoom_Handler;

import java.net.ServerSocket;
import java.net.Socket;

class Account extends Thread {
    private Socket clientSocket = null;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1024)) {
            while (true) {
                clientSocket = serverSocket.accept();
                new Account_Handler(clientSocket).start();
            }
        } catch(Exception exception) {
                exception.printStackTrace();
        }
    }
}

class Board extends Thread {
    private Socket clientSocket = null;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1025)) {
            while (true) {
                clientSocket = serverSocket.accept();
                new Board_Handler(clientSocket).start();
            }
            
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
}

class ChatRoom extends Thread {
    private Socket clientSocket = null;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1026)) {
            while (true) {
                clientSocket = serverSocket.accept();
                new ChatRoom_Handler(clientSocket).start();
            }

        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
}

class BoardInfo extends Thread {
    private Socket clientSocket = null;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1027)) {
            while (true) {
                clientSocket = serverSocket.accept();
                new Board_Handler(clientSocket).start();
            }

        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
}

public class ServerMain {
    public static void main(String[] args) {
        new Account().start();
        new Board().start();
        new BoardInfo().start();
    }
}