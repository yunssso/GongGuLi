package back.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ChatServer {
    private ServerSocket ss;
    private ArrayList<ChatHandler> list;

    public ChatServer(){
        try {
            ss = new ServerSocket(4444);

            list = new ArrayList<ChatHandler>();

            while(true) {
                System.out.println("[클라이언트 접속 대기중]");

                Socket socket = ss.accept();
                System.out.println("Client connect : " + socket.getLocalAddress());

                ChatHandler handler = new ChatHandler(socket, list);//스레드 생성
                handler.start();//스레드 시작

                list.add(handler);
            }//while
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }
}