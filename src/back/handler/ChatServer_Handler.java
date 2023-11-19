package back.handler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer_Handler extends Thread {
	private BufferedReader in = null;
	private PrintWriter out = null;
	private Socket socket = null;
	private ArrayList<ChatServer_Handler> list = null;
	private String name = null;
    
	public ChatServer_Handler(Socket socket, ArrayList<ChatServer_Handler> list) {
		this.socket = socket;
		this.list = list;

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			name = in.readLine();
			sendParticipants();
			sendAll(name + "이(가) 입장했습니다.");

			if (list.size() == 1) {
				sendAll("MASTER:" + name);
				name = name + "(방장)";
				sendParticipants();
			}

			String line = null;
			while (true) {
				line = in.readLine();
				
				if (line.toUpperCase().contains("EXIT:") || line == null) {
					if (line.replace("EXIT:", "").equals(name)) {
						break;
					}
				}

				if (line.contains("KICKNAME:")) {	
					String target_name = line.replace("KICKNAME:", "");
					for (ChatServer_Handler handler : list) {
						if (handler.getName(true).equals(target_name)) {
							sendAll(target_name + "을(를) 강퇴했습니다.");
							sendAll("KICKNAME:" + target_name);
							list.remove(handler);
							sendParticipants();
							System.out.println("Client unconnect (kicked) : " + handler.socket.getLocalAddress());
							break;
						}
					}
				} else {
					sendAll("[ " + name + " ] " + line);
				}
			}

			list.remove(this);
			sendParticipants();
			sendAll(name + "이(가) 퇴장했습니다.");

			in.close();
			out.close();
			socket.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private String getName(boolean b) {
		return name;
	}
	
	private void sendAll(String msg) {
		for (ChatServer_Handler handler : list) {
			handler.out.println(msg);
			handler.out.flush();
		} // for
	}

	private void sendParticipants() {
		String names = "USERNAME:";

		for (ChatServer_Handler handler : list) {
			names += handler.getName(true) + " ";
			handler.out.println("USERCOUNT:" + list.size());
			handler.out.flush();
		}
		
		for (ChatServer_Handler handler : list) {
			handler.out.println(names);
			handler.out.flush();
		}
		
	}
}