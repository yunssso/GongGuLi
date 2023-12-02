package main;

import serverStructure.*;

public class ServerMain {
    public static void main(String[] args) {
        new Account().start();
        new Board().start();
        new ChatRoom().start();
        new BoardInfo().start();
        new MyPage().start();
    }
}

//  12-03 05:30