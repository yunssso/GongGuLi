package main;

import serverStructure.Account;
import serverStructure.Board;
import serverStructure.BoardInfo;
import serverStructure.ChatRoom;

public class ServerMain {
    public static void main(String[] args) {
        new Account().start();
        new Board().start();
        new BoardInfo().start();
        new ChatRoom().start();
    }
}