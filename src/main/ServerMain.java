package main;

import server_structure.Account;
import server_structure.Board;
import server_structure.BoardInfo;
import server_structure.ChatRoom;

public class ServerMain {
    public static void main(String[] args) {
        new Account().start();
        new Board().start();
        new BoardInfo().start();
        new ChatRoom().start();
    }
}